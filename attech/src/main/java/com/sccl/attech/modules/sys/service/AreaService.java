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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.modules.sys.dao.AreaDao;
import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @author sccl
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

	@Autowired
	private AreaDao areaDao;
	
	public Area get(String id) {
		return areaDao.get(id);
	}
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		
		area.setParent(this.get(area.getParentId()));
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		areaDao.clear();
		areaDao.save(area);
		// 更新子节点 parentIds
		List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		areaDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	@Transactional(readOnly = false)
	public void saveOnly(Area area){
		
		areaDao.saveOnly(area);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		areaDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	/**
	 * 区域分页查询
	 * @param page
	 * @param area
	 * @return
	 */
	public Page<Area> findArea(Page<Area> page, Area area) {
		DetachedCriteria dc = areaDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(area.getName())){
			dc.add(Restrictions.like("name", "%" + area.getName() + "%"));
		}
		if (StringUtils.isNotEmpty(area.getType())){
			dc.add(Restrictions.like("type", "%" + area.getType() + "%"));
		}
		dc.createAlias("officeList", "officeList", JoinType.LEFT_OUTER_JOIN);
		//dc.add(dataScopeFilter(UserUtils.getUser(), "officeList", "createBy"));
		dc.add(Restrictions.eq(Area.FIELD_DEL_FLAG, Area.DEL_FLAG_NORMAL));
		
		return areaDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param area
	 * @return
	 */
	public List<Area> getList(Area area){
       DetachedCriteria dc = areaDao.createDetachedCriteria();		
		if (StringUtils.isNotEmpty(area.getName())){
			dc.add(Restrictions.like("name", "%" + area.getName() + "%"));
		}		
		dc.add(Restrictions.eq(Area.FIELD_DEL_FLAG, Area.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return areaDao.find(dc);
	}

	public List<Area> findProvince(String parentId, String type) {
		return areaDao.findChildes(parentId, type);
	}
	
//	/**
//	 * 获取区域树
//	 * @param allTreeVo 区域树视图模型
//	 * @param area  根节点区域
//	 * @param extId 当前区域id
//	 * @return
//	 */
//	public AllTreeVo getAreaTree(AllTreeVo allTreeVo,Area area,String extId){
//		if(allTreeVo==null){
//			List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
//			allTreeVo = new AllTreeVo(area,extId);
//			for (Area o : area.getChildList()) {
//				AllTreeVo t = new AllTreeVo(o,extId);
//				treeVos.add(t);
//			}
//			allTreeVo.setChildren(treeVos);
//			allTreeVo = getAreaTree(allTreeVo,null,extId);
//		}else{
//			for (AllTreeVo treeVo : allTreeVo.getChildren()) {
//				List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
//				Area off = areaDao.get(treeVo.getId());
//				for (Area o : off.getChildList()) {
//					AllTreeVo treeVo1 = new AllTreeVo(o,extId);
//					treeVos.add(treeVo1);
//				}
//				treeVo.setChildren(treeVos);
//				treeVo = getAreaTree(treeVo,null,extId);
//			}
//		}
//		return allTreeVo;
//	}
	
	/**
	 * 根据type查询所有区域信息
	 * @author luoyang
	 * @param type
	 * @return
	 */
	public List<Area> findListByType(String type){
		DetachedCriteria dc =areaDao.createDetachedCriteria();
		dc.add(Restrictions.eq("type", type));
		
		return areaDao.find(dc);
	}
	//private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	/**
	 * 根据数字类型的区域类型返回中文类型的区域类型，用于导出数据时显示
	 * @param type
	 * @return
	 */
	public String getType(String type){
		String typeValue="";
		if(StringUtils.isNotBlank(type)){
			if(type.equals("1")){
				typeValue="国家";
			}
			else if(type.equals("2")){
				typeValue="省份、直辖市";
			}
			else if(type.equals("3")){
				typeValue="地市";
			}
			else if(type.equals("4")){
				typeValue="地市";
			}
		}
		
		return typeValue;
	}
	/**
	 * 省管调用接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param parentId
	 * @param type
	 * @param code
	 * @param name
	 * @param id
	 * @return
	 */
	public String remoteArea(String OPFlag, String TimeStamp, String hashCode,String Summary, String parentId, String type, String code,
			String name, String id,String parentType) {
		String md5 = Md5Util.md5Encoder(TimeStamp+"cj2015");
		if(md5.equalsIgnoreCase(hashCode)){
			System.out.println(true);
			if(OPFlag.equals("0100") || OPFlag.equals("0102")){ // 默认批量导入数据
				Area area = new Area();
				area.setOutId(id);
				area.setName(name);
				Area parent = new Area();
				if(parentId == null){
					parent.setId("1");
					area.setParentIds("1");
				}
				
				area.setCode(code);
				area.setRemarks(Summary);
				area.setType(type);
				if(!type.equals("2")){ //假如type不等于2说明不是省，就必须往parendIds里面加入数据
					Area aa = areaDao.findByOutId(parentType, parentId);
					//System.out.println("==============="+id+"=="+name+"=="+parentId+"=="+parentType);
					area.setParent(aa);
					Area a = areaDao.get(aa.getId());
					if(a != null){
						StringBuilder sb = new StringBuilder();
						sb.append(a.getParentIds() == null ?"":a.getParentIds()).append(",").append(aa.getId());
						area.setParentIds(sb.toString());
					}
					
				}
				areaDao.saveOnly(area);
				
				return "0000"; //处理成功
			}else if(OPFlag.equals("0103")){ //修改
				Area area = new Area();
				area.setOutId(id);
				area.setName(name);
				Area parent = new Area();
				if(parentId == null){
					parent.setId("1");
					area.setParentIds("1");
				}else{
					parent.setId(parentId);
				}
				area.setParent(parent);
				area.setCode(code);
				area.setRemarks(Summary);
				area.setType(type);
				if(!type.equals("2")){ //假如type不等于2说明不是省，就必须往parendIds里面加入数据
					Area a = areaDao.get(parentId);
					if(a != null){
						StringBuilder sb = new StringBuilder();
						sb.append(a.getParentIds() == null ?"":a.getParentIds()).append(",").append(parentId);
						area.setParentIds(sb.toString());
					}
					
				}
				areaDao.save(area);
			}else if(OPFlag.equals("0101")){ //删除
				delete(id);
			}
			return "00000";
			
		}else{
			System.out.println(false);
			return "00003"; //业务未授权
		}
	}
	
	//导出文件
	public String exportFile(Area area , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"area.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "区域名称");
			e.setTitleCell(2, "区域编码");
			e.setTitleCell(3, "区域类型");	
			e.setTitleCell(4, "备注");	
			e.setTitleCell(5, "上级区域名称");
			List<Area> list=getList(area);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getName());
					if(list.get(i-1).getCode()==null){
						e.setCell(2, "  ");
					}else{
						e.setCell(2, list.get(i-1).getCode());
					}					
					String type=list.get(i-1).getType();
					String typeValue=getType(type);
					e.setCell(3, typeValue);
					if(list.get(i-1).getRemarks()==null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getRemarks());
					}					
					e.setCell(5, list.get(i-1).getParentName());
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
	
}
