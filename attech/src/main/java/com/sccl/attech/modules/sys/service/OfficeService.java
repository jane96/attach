/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.modules.sys.dao.OfficeDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.dao.UserOfficeDao;
import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.TreeVO;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author sccl
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private UserOfficeDao userOfficeDao;
	@Autowired
	private UserDao userDao;
	
	public Office get(String id) {
		return officeDao.get(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	@Transactional(readOnly = false)
	public void save(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		officeDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	/**
	 * 根据类型查询所有 office
	 * @author luoyang
	 * @param type 类型：（1：公司；2：部门；3：小组）
	 * @return office集合
	 */
	public List<Office> findAllByType(String type){
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(type)){
			dc.add(Restrictions.eq("type", type));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		
		return officeDao.find(dc);
	}

	public Page<Office> find(Page<Office> page, Office office) {
		User user = UserUtils.getUser();
//		if (user.isAdmin()){
//			officeList = officeDao.findAllList();
//		}else{
//			officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
//		}
		
		DetachedCriteria dc = officeDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(office.getName())){
//			dc.add(Restrictions.like("name", "%"+office.getName()+"%"));
//		}
		dc.add(dataScopeFilter(user, "office", "createBy"));//权限控制
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(page, dc);
	}

	/**
	 * 查询出部门-用户数
	 * @param parentId
	 * @param type
	 * @return
	 */
	public Map<String,Object> getChildren(String companyId,String parentId, String type) {
		
		String deptId="";
				
		//所选部门或公司
		List<Office> list = new ArrayList<Office>();
		
		if(parentId!=null&&!parentId.equals(""))
		{
			list = officeDao.find("from Office where parent.id = '"+parentId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
			
			deptId = parentId;
		}
		else
		{
			if(companyId!=null&&!companyId.equals(""))
			{
				list = officeDao.find("from Office where delFlag='"+Office.DEL_FLAG_NORMAL+"' and id='"+companyId+"'");
				
				deptId = companyId;
			}
			else
			{
				list = officeDao.find("from Office where parent.id = null and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
				
				deptId = list.get(0).getId();
			}
		}
		
		String sql="select distinct u.* from sys_office o,sys_user_office uo,sys_user u where o.id=uo.office_id and u.id=uo.user_id  and (o.parent_ids like '%"+deptId+"%' or o.id='"+deptId+"')  and o.del_flag='"+Office.DEL_FLAG_NORMAL+"' and u.del_flag='"+User.DEL_FLAG_NORMAL+"' and o.type!='1'";
		List<User> officeUsers =  this.userDao.findBySql(sql, null, User.class);
		
		String pid="";  
		for(Office o : list)
		{
			pid+="'"+o.getId()+"',";
		}
		
		List<Object[]> l = new ArrayList<Object[]>();
		if(!"".equals(pid))
		{
			pid = pid.substring(0,pid.length()-1);
			l = officeDao.find("select o.parent.id,count(id)  from Office o where o.parent.id in ("+pid+") group by o.parent.id" );
		}
		
		Map<String,String> hasMap = new HashMap<String,String>();
		for(Object[] os : l)
		{
			String p_id = String.valueOf(os[0]);
			String p_count = String.valueOf(os[1]);
			hasMap.put(p_id, p_count);
		}
		
		
		List<TreeVO> listTree = new ArrayList<TreeVO>();
		for(Office o : list)
		{
			if(hasMap.get(o.getId())!=null)
			{
				o.setHasChildren("true");
			}
			else
			{
				o.setHasChildren("false");
			}
			
			TreeVO tv = new TreeVO();
			tv.setHasChildren(o.getHasChildren());
			tv.setId(o.getId());
			tv.setName(o.getName());
			tv.setLabel(o.getShortName());
			tv.setType("dept");
			listTree.add(tv);
			
		}
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("depts", listTree);
		map.put("users", officeUsers);
		return map;
	}
	
	
	
	
	public Map<String,Object> getChildrens(String parentId, String type) {
		List<Office> list = new ArrayList<Office>();
		if(StringUtils.isNotEmpty(parentId)){
			list = officeDao.findBySql("select * from SYS_OFFICE1 s where  DEL_FLAG='"+Office.DEL_FLAG_NORMAL+"' START with s.PARENT_ID='"+parentId+"' CONNECT BY PRIOR s.ID=s.PARENT_ID",null,Office.class);
		//	list = officeDao.find("from Office where parent.id = '"+parentId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
		}else{
			list = officeDao.find("from Office where parent.id = null and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
		}
		//把获取的公司里面的所有人员得到
		List<User> officeUsers = new ArrayList<User>();
		if(!"0".equals(parentId) && !"null".equals(parentId) && !"".equals(parentId))
		{
			Office o = officeDao.get(parentId);
			officeUsers = o.getUserList();
		}
		String pid="";  
		for(Office o : list)
		{
			pid+="'"+o.getId()+"',";
			officeUsers.addAll(o.getUserList());
		}
		List<Object[]> l = new ArrayList<Object[]>();
		if(!"".equals(pid))
		{
			pid = pid.substring(0,pid.length()-1);
			l = officeDao.find("select o.parent.id,count(id)  from Office o where o.parent.id in ("+pid+") group by o.parent.id" );
		}
		Map<String,String> hasMap = new HashMap<String,String>();
		for(Object[] os : l)
		{
			String p_id = String.valueOf(os[0]);
			String p_count = String.valueOf(os[1]);
			hasMap.put(p_id, p_count);
		}
		
		
		List<TreeVO> listTree = new ArrayList<TreeVO>();
		for(Office o : list)
		{
			if(hasMap.get(o.getId())!=null)
			{
				o.setHasChildren("true");
			}
			else
			{
				o.setHasChildren("false");
			}
			TreeVO tv = new TreeVO();
			tv.setHasChildren(o.getHasChildren());
			tv.setId(o.getId());
			tv.setLabel(o.getName());
			tv.setType("dept");
			listTree.add(tv);
		}
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("depts", listTree);
		map.put("users", officeUsers);
		return map;
	}
	
	
	
	
	/**
	 * 查询出部门树
	 * @param parentId
	 * @param type
	 * @return
	 */
	public List<Office> getTreeData(String parentId,String companyId , String type) {
		List<Office> list = null;
		if(parentId!=null && !parentId.equals(""))
		{
			list = officeDao.find("from Office where parent.id = '"+parentId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
		}
		else
		{
			list = officeDao.find("from Office where id = '"+companyId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
		}
		
		if(list.size()==0) 
		{
			return list;
		}
		
		String pid="";
		for(Office o : list)
		{
			pid+="'"+o.getId()+"',";
		}
		
		pid = pid.substring(0,pid.length()-1);
		
		List<Object[]> l = officeDao.find("select o.parent.id,count(id)  from Office o where o.parent.id in ("+pid+") and delFlag='"+Office.DEL_FLAG_NORMAL+"' group by o.parent.id " );
		Map<String,String> hasMap = new HashMap<String,String>();
		
		for(Object[] os : l)
		{
			String p_id = String.valueOf(os[0]);
			String p_count = String.valueOf(os[1]);
			hasMap.put(p_id, p_count);
		}
		for(Office o : list)
		{
			if(hasMap.get(o.getId())!=null)
			{
				o.setHasChildren("true");
			}
			else
			{
				o.setHasChildren("false");
			}
		}
		
		return list;
	}
	
	/**
	 * 根据部门Id，查询出部门所属的直接公司
	 * @param deptId
	 * @return
	 */
	public Office getCompanyById(String deptId){
		String hql = "select o from Office o where o.id = (select oo.parent.id from Office oo where oo.id='"+deptId+"' ) and o.delFlag='"+Office.DEL_FLAG_NORMAL+"'";
		Office office = new Office();
		office = officeDao.getByHql(hql);
		
		Office companyByOffice = getCompanyByOffice(office);
		
		return companyByOffice;
	}
	
	/**
	 * 递归查询出部门所属的直接公司
	 * @param office
	 * @return
	 */
	public Office getCompanyByOffice(Office office){
		if("1".equals(office.getType())){
			return office;
			
		}else{ 
			Office parent = office.getParent();
			return getCompanyByOffice(parent);
		}
		
	}
	
	
//	/**
//	 * 获取部门树
//	 * @param allTreeVo 部门树视图模型
//	 * @param office  根节点部门
//	 * @param extId 当前部门id
//	 * @return
//	 * deng
//	 */
//	public AllTreeVo getOfficeTree(AllTreeVo allTreeVo,Office office,String extId){
//		if(allTreeVo==null){
//			List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
//			allTreeVo = new AllTreeVo(office,extId);
//			for (Office o : office.getChildren()) {
//				AllTreeVo t = new AllTreeVo(o,extId);
//				treeVos.add(t);
//			}
//			allTreeVo.setChildren(treeVos);
//			allTreeVo = getOfficeTree(allTreeVo,null,extId);
//		}else{
//			for (AllTreeVo treeVo : allTreeVo.getChildren()) {
//				List<AllTreeVo> treeVos  = new ArrayList<AllTreeVo>();
//				Office off = officeDao.get(treeVo.getId());
//				for (Office o : off.getChildren()) {
//					AllTreeVo treeVo1 = new AllTreeVo(o,extId);
//					treeVos.add(treeVo1);
//				}
//				treeVo.setChildren(treeVos);
//				treeVo = getOfficeTree(treeVo,null,extId);
//			}
//		}
//		return allTreeVo;
//	}
	
	
	/**
	 * ajax获取部门树
	 * @param allTreeVo 部门树视图模型
	 * @param office  根节点部门
	 * @param extId 当前部门id
	 * @return
	 * deng
	 */
	public List<Office> getOfficeTree(String parentId,String companyId, String type){
		
		String parentIds=null;
		
		List<Office> list = new ArrayList<Office>();
		if(parentId!=null&&!parentId.equals(""))
		{
			list = officeDao.find("from Office where parent.id = '"+parentId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
			
			parentIds=parentId;
		}
		else
		{
			if(companyId!=null&&!companyId.equals(""))
			{
				list = officeDao.find("from Office where delFlag='"+Office.DEL_FLAG_NORMAL+"' and id='"+companyId+"'");
				
				parentIds=companyId;
			}
			else
			{
				list = officeDao.find("from Office where parent.id = null and delFlag='"+Office.DEL_FLAG_NORMAL+"'");
			}
		}
		
		if(list.size()<=0)
		{
			 return list;
		}
		
		String pid="";
		for(Office o : list)
		{
			pid+="'"+o.getId()+"',";
		}
		
		pid = pid.substring(0,pid.length()-1);
		List<Object[]> l = officeDao.find("select o.parent.id,count(id)  from Office o where o.parent.id in ("+pid+") and delFlag='0'  group by o.parent.id " );
		Map<String,String> hasMap = new HashMap<String,String>();
		for(Object[] os : l)
		{
			String p_id = String.valueOf(os[0]);
			String p_count = String.valueOf(os[1]);
			hasMap.put(p_id, p_count);
		}
		
		Office company=null;
		if(parentIds!=null)
		{
			company=this.getOfficeCompany(parentIds);
		}
		
		for(Office o : list)
		{
			if(hasMap.get(o.getId())!=null)
			{
				o.setHasChildren("true");
			}
			else
			{
				o.setHasChildren("false");
			}
			
			if(company!=null)
			{
				o.setCompanyId(company.getId());
			}
		}
		return list;
	}
	
	
	/**
	 * 查询的如果是公司则返回当前公司信息，如果查询的是非公司，则返回改机构所属的直属公司
	 * @param officeId
	 * @return
	 */
	public Office getOfficeCompany(String officeId)
	{
		Office office= this.officeDao.get(officeId);
		if(office != null && !office.getType().equals("1"))
		{
			office = this.getOfficeCompany(office.getParentId());
		}
		return office;
	}
	
	/**
	 * ajax获取公司树
	 * @param allTreeVo 公司树视图模型
	 * @param office  根节点部门
	 * @param extId 当前公司id
	 * @return
	 * deng
	 */
	public List<Office> getCompanyTree(String parentId,String companyId,String type){
		
		List<Office> list = new ArrayList<Office>();
		
		if(StringUtils.isNotBlank(parentId))
		{
			list = officeDao.find("from Office where parent.id = '"+parentId+"' and delFlag='"+Office.DEL_FLAG_NORMAL+"' and type='1'");
		}
		else
		{
			list = officeDao.find("from Office where delFlag='"+Office.DEL_FLAG_NORMAL+"' and type='1' and id='"+companyId+"'");
		}
		
		List<Office> officeList= new ArrayList<Office>();
		
		for(Office o : list)
		{
			List<Office> childrenList = officeDao.find("from Office o where o.parent.id ='"+o.getId()+"' and delFlag='0' and type='1' group by o.parent.id " );
			if(childrenList.size()==0)
			{
				o.setHasChildren("false");
			}
			else
			{
				o.setHasChildren("true");
				o.setChildren(childrenList);
			}
			
			officeList.add(o);
		}
		
		return officeList;
	}
	
	/**
	 * 设置部门的负责人
	 * @param oneoffice
	 */
	@Transactional(readOnly = false)
	public void updateMaster(Office office) {
		String hql = "update Office  set master='"+office.getMaster()+"' where id = '"+office.getId()+"'";
		officeDao.update(hql);
	}
	/**
	 * 省管接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param parentId
	 * @param id
	 * @param type
	 * @param areaId
	 * @param code
	 * @param name
	 * @param grade
	 * @param address
	 * @param zipCode
	 * @param master
	 * @param phone
	 * @param fax
	 * @param email
	 * @param industry
	 * @param sort
	 * @return
	 */
	public String remoteOffice(String OPFlag, String TimeStamp,String hashCode, String Summary, String parentId, String id,
			String type, String areaId, String code, String name, String grade,String address, String zipCode, String master, String phone,
			String fax, String email, String industry, Integer sort,String mark,Integer level) {
		String md5 = Md5Util.md5Encoder(TimeStamp+"cj2015");
		if(md5.equalsIgnoreCase(hashCode)){
			if(OPFlag.equals("0100") || OPFlag.equals("0102")){ // 默认批量导入数据
				Office office = new Office();
				office.setRemarks(Summary);
				if(parentId != null && !"0".equals(parentId)){
					Office oo = null;
					if(StringUtils.isNotBlank(mark)){
						oo = officeDao.findByOutId(mark, parentId);
					}else{
						oo = officeDao.findByOutId(type, parentId);
					}
					office.setParent(oo);
					//如果父类ID不为空，找到所有parendIds
					Office o = officeDao.get(oo.getId());
					if(o != null){
						StringBuilder sb = new StringBuilder();
						sb.append(o.getParentIds() == null ?"":o.getParentIds()).append(",").append(oo.getId());
						office.setParentIds(sb.toString());
					}
				}
				office.setLevel(level);
				office.setOutId(id);
				office.setType(type);
				office.setAreaId(areaId);
				office.setCode(code);
				office.setName(name);
				office.setGrade(grade);
				office.setAddress(address);
				office.setZipCode(zipCode);
				office.setMaster(master);
				office.setPhone(phone);
				office.setFax(fax);
				office.setEmail(email);
				office.setIndustry(industry);
				office.setSort(sort);
				office.setRemarks("2"); //标示是否外来数据
				officeDao.saveOnly(office);
			}else if(OPFlag.equals("0103")){ //修改
				Office office = new Office();
				office.setRemarks(Summary);
				Office parent = new Office();
				if(parentId != null){
					parent.setParentId(parentId);
					office.setParent(parent);
					//如果父类ID不为空，找到所有parendIds
					Office o = officeDao.get(parentId);
					if(o != null){
						StringBuilder sb = new StringBuilder();
						sb.append(o.getParentIds() == null ?"":o.getParentIds()).append(",").append(parentId);
						office.setParentIds(sb.toString());
					}
				}
				office.setLevel(level);
				office.setOutId(id);
				office.setType(type);
				office.setAreaId(areaId);
				office.setCode(code);
				office.setName(name);
				office.setGrade(grade);
				office.setAddress(address);
				office.setZipCode(zipCode);
				office.setMaster(master);
				office.setPhone(phone);
				office.setFax(fax);
				office.setEmail(email);
				office.setIndustry(industry);
				office.setSort(sort);
				office.setRemarks("2"); //标示是否外来数据
				officeDao.save(office);
			}else if(OPFlag.equals("0101")){ //删除
				officeDao.deleteById(id);
			}
			return "00000";
		}else{
			return "00003"; //业务未授权
		}
		
	}
	
	public List<Map<String, Object>> getUserDeptList(String userId) throws Exception
	{
		List<Map<String, Object>> officeList=new ArrayList<Map<String, Object>>();
		
		User user = UserUtils.getUser();
		if(user == null || user.getCompany() == null)
		{
			return officeList;
		}
		
		String idsStr = this.officeDao.getCompanyIds(user.getCompany().getId());
		
		String officeSql="select uo.* from sys_office o,sys_user_office uo where o.id=uo.office_id and uo.user_id='"+userId+"'";
		List<UserOffice> userOfficeList= this.userOfficeDao.findBySql(officeSql,null,UserOffice.class);

		for(UserOffice userOffice:userOfficeList)
		{
			Office office	=	userOffice.getDept();
			Dict dict		=  	userOffice.getRole();
			Office company	=	userOffice.getCompany();
			
			if(user.getCompany()!=null && idsStr.indexOf(company.getId())!=-1)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", office.getId());
				map.put("name", office.getName());
				map.put("type", office.getType());
				map.put("companyId", company.getId());
				
				JSONObject object=new JSONObject();
				object.put("id", dict.getId());
				object.put("label", dict.getLabel());
				object.put("roles", dict.getRoles());
				object.put("hasChild", "false");
				
				map.put("bussiType",object);
				
				officeList.add(map);
			}
		}
		
		return officeList;
	}
	
	/**
	 * 查询公司及其公司下的所有部门
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<Office>> getCompanyAndOfficeList(String companyId) throws Exception
	{
		Map<String, List<Office>> map = new HashMap<String, List<Office>>();
		
		List<Office> companyList = null;
		
		if(companyId == null || companyId.equals(""))
		{
			//查询公司的根目录
			String sql ="select * from sys_office so where so.type ='1' and so.del_flag ='"+Office.DEL_FLAG_NORMAL+"' and so.parent_id is null";
			companyList = this.officeDao.findBySql(sql,Office.class);
			if(companyList.size()!=1)
			{
				throw new Exception("部门树根节点配置异常！"+companyList.size());
			}
			
			companyId = companyList.get(0).getId();
		}
		else
		{
			String sql = "select * from sys_office so where so.type ='1' and so.del_flag ='"+Office.DEL_FLAG_NORMAL+"' and so.parent_id = '"+companyId+"'";
			companyList = this.officeDao.findBySql(sql,Office.class);
		}
		
		map.put("companyList", companyList);
		
		//查询公司下面的所有部门（包含子公司的部门）
		String sql = "select * from sys_office so where so.type ='2' and so.del_flag ='"+Office.DEL_FLAG_NORMAL+"' and so.parent_ids like '%"+companyId+"%'";
		List<Office> officeList = this.officeDao.findBySql(sql,Office.class);
		
		map.put("officeList", officeList);
		
		return map;
		
	}
}
