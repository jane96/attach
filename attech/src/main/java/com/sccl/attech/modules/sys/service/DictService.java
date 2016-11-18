/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.mapper.JsonMapper;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.CacheUtils;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.ITreeNode;
import com.sccl.attech.common.utils.StrUtils;
import com.sccl.attech.common.utils.TreeNodeUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.modules.sys.dao.DictDao;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.DictUtils;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.sys.vo.DictVo;

/**
 * 字典Service
 * @author sccl
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {

	@Autowired
	private DictDao dictDao;
	
//	@Autowired
//	private MyBatisDictDao myBatisDictDao;
	
	public Dict get(String id) {
		// MyBatis 查询
//		return myBatisDictDao.get(id);
		// Hibernate 查询
		return dictDao.get(id);
	}
	
	/**
	 * 获取字典树形结构
	 * @param dict
	 * @return
	 * @throws Exception
	 */
	public List<DictVo> findTree(DictVo dict) throws Exception{

		
		if (StringUtils.isNotEmpty(dict.getType())){

			String label="";
			if(null!=dict.getLabel()){
				label=dict.getLabel();
			}
			List<Dict> listData=dictDao.find("from Dict where type='"+dict.getType()+"' and label like '%"+label+"%' and delFlag=0");
			
			//-------------------------------新建list集合，防止修改的数据被存入数据库---------------------------------
			String dictList = JsonMapper.getInstance().toJson(listData);
			
			JSONArray jsonarray = JSONArray.fromObject(dictList);
			List<DictVo> jsonList = (List<DictVo>)JSONArray.toCollection(jsonarray,DictVo.class);
			
			//-------------------------------------------------------------------------------------------
			
			List<DictVo> list=new ArrayList<DictVo>();//用来存放在label属性前面加了加了“--”的数据
			
			//----------------------调用树形结构生成类-------------------------------
			ITreeNode[] nodes = new ITreeNode[jsonList.size()];
			for (int i = 0; i < jsonList.size(); i++) {
				nodes[i] = (ITreeNode) jsonList.get(i);
			}
			
			TreeNodeUtils.SortNodeToTree(nodes);
			List treeList = Arrays.asList(nodes);
			//-----------------------------------------------------------------
			for (int i = 0; i < treeList.size(); i++) {
				DictVo dict2 = new DictVo();
				dict2 = (DictVo) treeList.get(i);
				if(dict2.depth>1){//如果深度大于1说明该节点不是父节点
					String children="";
					for(int j=1;j<dict2.depth;j++){//根据深度添加“--”
						children+="--";
					}
					dict2.setLabel(children+dict2.getLabel());//在label属性前面加上“--”
						
				}
				if(dict2.depth!=0){
					list.add(dict2);//把修改之后的数据存入新的list集合
				}
				
			}
			
			
			return list;
		}else{
			return null;
		}
	}
	
	public Page<Dict> find(Page<Dict> page, Dict dict) {
		// MyBatis 查询
//		dict.setPage(page);
//		page.setList(myBatisDictDao.find(dict));
//		return page;
		// Hibernate 查询
		
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		dc.setProjection(Projections.distinct(Projections.property("type")));
		if (StringUtils.isNotEmpty(dict.getLabel())){
			dc.add(Restrictions.like("label", "%"+dict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(dict.getType())){
			dc.add(Restrictions.eq("type", dict.getType()));
		}
		System.out.println(dict.getType());
		if (StringUtils.isNotEmpty(dict.getDescription())){
			dc.add(Restrictions.like("description", "%"+dict.getDescription()+"%"));
		}
		
//		if(StringUtils.isNotEmpty(dict.getParentId())){
//			dc.add(Restrictions.sqlRestriction("parent_id = '"+dict.getParentId()+"'"));
//			dc.add(Restrictions.like("parentIds", "%"+dict.getParentId()+"%"));//查询父级节点组里面有没有顶级节点
//		}
		
//		dc.add(Restrictions.sqlRestriction("parent_ids is not null"));
		dc.add(Restrictions.sqlRestriction("del_flag=0"));
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		
		dc.setResultTransformer(dc.DISTINCT_ROOT_ENTITY);
		return dictDao.find(page, dc);
	}
	
	public List<Dict> findChildren(String id){
		
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		
		dc.add(Restrictions.sqlRestriction("parent_id = '"+id+"'"));//根据父节点id查找有没有子节点
		
		return dictDao.find(dc);
	}
	
	
	/**
	 * 用于导出数据的查询
	 * @param dict
	 * @return
	 */
	public List<Dict> getDicts(Dict dict){
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(dict.getLabel())){
			dc.add(Restrictions.like("label", "%"+dict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(dict.getType())){
			dc.add(Restrictions.like("type", "%"+dict.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(dict.getDescription())){
			dc.add(Restrictions.like("description", "%"+dict.getDescription()+"%"));
		}
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort"));
		return dictDao.find(dc);
		
	}
	public List<String> findTypeList(){
		return dictDao.findTypeList();
	}
	
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		dictDao.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dictDao.deleteById(id);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	//导出文件
	public String exportFile(Dict dict, HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"dict.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "键值");
			e.setTitleCell(2, "标签");	
			e.setTitleCell(3, "类型");	
			e.setTitleCell(4, "描述");
			e.setTitleCell(5, "排序");
			List<Dict> list=getDicts(dict);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getValue());
					e.setCell(2, list.get(i-1).getLabel());
					e.setCell(3, list.get(i-1).getType());
					if(list.get(i-1).getDescription()==null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getDescription());
					}
					if(list.get(i-1).getSort()==null){
						e.setCell(5, "  ");
					}else{
						e.setCell(5, list.get(i-1).getSort());
					}					
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}
	
	
	/**
	 * 一次返回树结构
	 */
	public List<AllTreeVo> getAllTreeList(Dict dict,String companyId){
		return null;
	}
	
	/**
	 * 分层返回树结构
	 * param dict中没有ID则查询父节点，表示第一次获取
	 * dict  type参数
	 */
	public List<AllTreeVo> getTreeList(Dict dict,String companyId){
		List<Dict> list = null;
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if(!"".equals(StrUtils.null2String(dict.getType()))){
		   dc.add(Restrictions.eq("type", dict.getType()));
		}
		if(!"".equals(StrUtils.null2String(dict.getId()))){
//			dc.add(Restrictions.eq("parent", dict));
			dc.add(Restrictions.sqlRestriction("parent_id = '"+dict.getId()+"'"));
		}else {
//			dc.add(Restrictions.isNull("parent"));
			dc.add(Restrictions.sqlRestriction("parent_id is null"));
		}
		dc.add(Restrictions.or(Restrictions.isNull("companyId"),Restrictions.eq("companyId", "0"),Restrictions.eq("companyId", StrUtils.null2String(companyId))));
		dc.add(Restrictions.eq("delFlag", "0"));
		list = dictDao.find(dc);
		List<AllTreeVo> listAllTreeVos = new ArrayList<AllTreeVo>();
        if(null != list && list.size() > 0 ){
			for(Dict dicts:list){
				AllTreeVo allTreeVo = new AllTreeVo();
				allTreeVo.setLabel(dicts.getLabel());
				allTreeVo.setId(dicts.getId());
				//判断是否拥有子节点
				List<Dict> dictChildren = findChildren(dicts.getId());//根据父节点id查找子节点
//				if(null != dicts.getChildren() && dicts.getChildren().size() > 0){
//					allTreeVo.setHasChild(true);
//				}
				if(null != dictChildren && dictChildren.size() > 0){//用于替换之前的判断，如果有子节点就执行下面的操作
					allTreeVo.setHasChild(true);
				}
				listAllTreeVos.add(allTreeVo);
			}
		}
		return listAllTreeVos;
	}
	
	
	
	/**
	 * 获取所有子节点（不包含父节点）
	 */
	public List<AllTreeVo> getAllChildTreeList1(Dict dicts,String companyId,String name){
		String hql = "FROM Dict WHERE 1=1 ";
		if(!"".equals(StrUtils.null2String(dicts.getType()))){
			//hql += " AND type = '"+dicts.getType()+"'";
		}
		System.out.println(name);
		if(name!=null && !name.equals(""))
		{
			hql += " AND label like '%"+name+"%'";
		}
		//delFlag
		//hql += " AND delFlag = '0'";
		
		
 		List<Dict> list = dictDao.findrnumber(hql, 10);
		List<AllTreeVo> listAllTreeVo = new ArrayList<AllTreeVo>();
		if(null != list && list.size() > 0){
			for(Dict dict:list){
				AllTreeVo allTreeVo = new AllTreeVo();
			    allTreeVo.setId(dict.getId());
			    allTreeVo.setLabel(dict.getLabel());
			    listAllTreeVo.add(allTreeVo);
			}
		}
		return listAllTreeVo;
	}
	
	
	
	
	public List<Dict> findDictType( Dict dict) {
		
		List<Dict> ftlist = dictDao.findDicType("parentIds is null");
		
		return ftlist;
	}
	
	
	/**
	 * 获取所有子节点（不包含父节点）
	 */
	public List<AllTreeVo> getAllChildTreeList(Dict dicts,String companyId,String name){
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if(!"".equals(StrUtils.null2String(dicts.getType()))){
			dc.add(Restrictions.eq("type", dicts.getType()));
		}
		
		if(name!=null && !name.equals(""))
		{
			dc.add(Restrictions.like("label", "%"+name.trim()+"%"));
		}
		dc.add(Restrictions.eq("delFlag", "0"));
		dc.add(Restrictions.or(Restrictions.isNull("companyId"),Restrictions.eq("companyId", "0"),Restrictions.eq("companyId", StrUtils.null2String(companyId))));
		List<Dict> list = dictDao.find(dc);
		List<AllTreeVo> listAllTreeVo = new ArrayList<AllTreeVo>();
		if(null != list && list.size() > 0){
			for(Dict dict:list){
				
				List<Dict> dictChildren = findChildren(dict.getId());//根据父节点id查找子节点
				
//				if(null == dict.getChildren() || dict.getChildren().size() <= 0){
				if(null == dictChildren || dictChildren.size() <= 0){//用于替换之前的判断，如果没有子节点就执行下面的操作
				    AllTreeVo allTreeVo = new AllTreeVo();
				    allTreeVo.setId(dict.getId());
				    allTreeVo.setLabel(dict.getLabel());
				    listAllTreeVo.add(allTreeVo);
				}
			}
		}
		return listAllTreeVo;
	}
	
	public List<AllTreeVo> getPropTypeByIds(String ids)
	{
		List<AllTreeVo> listAllTreeVo = new ArrayList<AllTreeVo>();
		if(ids == null || ids.equals(""))
		{
			return listAllTreeVo;
		}
		List<Dict> d = dictDao.find("FROM Dict WHERE id in("+getnewids( ids, ",")+")");
		if(d != null && d.size() > 0)
		{
			for(Dict dict:d)
			{
				AllTreeVo allTreeVo = new AllTreeVo();
			    allTreeVo.setId(dict.getId());
			    allTreeVo.setLabel(dict.getLabel());
			    listAllTreeVo.add(allTreeVo);
			}
		}
		return listAllTreeVo;
	}
	
	
	//自己用的方法，把一些逗号隔开的id，换成('1','2')这种形式的，方便in查询
	private String getnewids(String ids,String split)
	{
		String uids[] = ids.split(split);
		ids = "";
		for(String id : uids)
		{
			if(id!=null&&!"".equals(id))
			{
				ids += "'"+id+"',";
			}
		}
		ids = ("".equals(ids))?"":ids.substring(0,ids.length()-1);
		return ids;
	}
}
