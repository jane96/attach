/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;

/**
 * 机构DAO接口
 * @author sccl
 * @version 2013-8-23
 */
@Repository
public class OfficeDao extends BaseDao<Office> {
	
	public List<Office> findByParentIdsLike(String parentIds){
		return find("from Office where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Office> findChildren(String parentId, String type) {
		return find("from Office where delFlag=:p1 and (parent.id=:p2 or type=:p3)", 
				new Parameter(Office.DEL_FLAG_NORMAL, parentId, type));
	}
	@SuppressWarnings("unchecked")
	public Office findByOutId(String type,String outId){
		String hql = "from Office where type=? and outId=?";;
		List<Office> offices = getSession().createQuery(hql).setParameter(0, type).setParameter(1, outId).list();
		if(offices != null && offices.size() > 0){
			return offices.get(0);
		}
		return null;
	}
	
	public Page<User> getPageUserList(Page<User> page,String officeId,Map<String,Object> map)
	{
			String userSql="";
			String deptSql="";
			
			if(map.get("loginName")!=null)
			{
				userSql +=" and  u.login_name like '"+map.get("loginName")+"'";
			}
			
			if(map.get("name")!=null)
			{
				userSql +=" and  u.name like '"+map.get("name")+"'";
			}
			
			if(map.get("officeName")!=null)
			{
				deptSql +=" and (o.name like '"+map.get("officeName")+"' or o1.name like '"+map.get("officeName")+"')";
			}
			else
			{
				if(officeId!=null&&!officeId.equals(""))
				{
					userSql +=" and (o.parent_ids like '%"+officeId+"%' or o.id='"+officeId+"') ";
				}
			}
			
			if(map.get("mobile")!=null)
			{
				userSql +=" and  u.mobile like '"+map.get("mobile")+"'";
			}
		
//			String  sqlUser="select distinct u.* from sys_office o,sys_user_office uo,sys_user u where uo.office_id=o.id and u.id=uo.user_id "+userSql+" and o.del_flag='"+User.DEL_FLAG_NORMAL+"'";
//			Page<User> userPage= this.findBySql(page, sqlUser,User.class);
//			
//			List<User> usersList= new ArrayList<User>();
//			
//			for(User user :userPage.getList())
//			{
//				String sqlDept="select uo.company_id,uo.office_id from sys_office o,sys_user_office uo,sys_office o1 where uo.office_id=o.id and uo.company_id=o1.id "+deptSql+" and uo.user_id='"+user.getId()+"'and o.del_flag='"+User.DEL_FLAG_NORMAL+"'";
//				List<Object[]> deptList= this.findBySql(sqlDept);
//				
//				for(Object[] arr:deptList)
//				{
//					User user1= user;
//					Office company= this.get(arr[0].toString());//公司
//					user1.setCompany(company);
//					user1.setCompanyName(company.getName());
//					
//					Office office= this.get(arr[1].toString());//部门
//					user1.setOffice(office);
//					user1.setOfficeName(office.getName());
//					
//					usersList.add(user1);
//				}
//			}
//			
//			userPage.setList(usersList);
			
//			String  sql="select  u.id,u.create_date,u.del_flag,u.remarks,u.update_date,u.area_setup_id,u.email,u.expiration_time,u.location_on,u.login_date,u.login_ip,u.login_name,u.mobile,u.name,u.no,u.out_id,u.password,u.phone,u.photo,u.state," +
//						"u.time_setup_id,u.user_type,u.create_by,u.update_by,o1.id company_id,o.id office_id"+
//						"from sys_office o,sys_user_office uo,sys_user u,sys_office o1 where uo.office_id=o.id and u.id=uo.user_id and uo.company_id=o1.id "+userSql+" "+deptSql+" and o.del_flag='"+User.DEL_FLAG_NORMAL+"' and o1.del_flag='"+User.DEL_FLAG_NORMAL+"'";

			String  sql="select uo.* from sys_office o,sys_user_office uo,sys_user u,sys_office o1 where uo.office_id=o.id and u.id=uo.user_id and uo.company_id=o1.id "+userSql+" "+deptSql+" and o.del_flag='"+User.DEL_FLAG_NORMAL+"' and o1.del_flag='"+User.DEL_FLAG_NORMAL+"' and u.del_flag='"+User.DEL_FLAG_NORMAL+"'";
			
			Page<UserOffice> pageList= this.findBySql(new Page<UserOffice>(page.getPageNo(),page.getPageSize(),page.getCount()),sql,UserOffice.class);
			
			List<User> userList=new ArrayList<User>();
			
			for(UserOffice userOffice:pageList.getList())
			{
				User oldUser = userOffice.getUser();
				Office company=userOffice.getCompany();
				Office office=userOffice.getDept();
				
				User user=new User();
				
				user.setId(oldUser.getId());
				user.setSearchFromPage(oldUser.isSearchFromPage());
				user.setRemarks(oldUser.getRemarks());
				user.setCreateDate(oldUser.getCreateDate());
				user.setUpdateDate(oldUser.getUpdateDate());
				user.setDelFlag(oldUser.getDelFlag());
				user.setLoginName(oldUser.getLoginName());
				user.setName(oldUser.getName());
				user.setUserType(oldUser.getUserType());
				user.setAreaSetupId(oldUser.getAreaSetupId());
				user.setTimeSetupId(oldUser.getTimeSetupId());
				user.setOutId(oldUser.getOutId());
				user.setRoleIds(oldUser.getRoleIds());
				user.setMobile(oldUser.getMobile());
				user.setLoginDate(oldUser.getLoginDate());
				user.setLoginIp(oldUser.getLoginIp());
				user.setPhoto(oldUser.getPhoto());
				user.setPhone(oldUser.getPhone());
				user.setRoleList(oldUser.getRoleList());
				user.setNo(oldUser.getNo());
				user.setUserOfficeId(userOffice.getId());
				
				if(company!=null && office!=null)
				{
					user.setCompany(company);
					user.setCompanyName(company.getName());
					
					user.setOffice(office);
					user.setOfficeName(office.getName());
				}
				
				userList.add(user);
			}
			
			page.setList(userList);
			return page;
		
	}
	
	/**
	 * 查询企业级部门及其子公司
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public String getCompanyIds(String companyId) throws Exception
	{
		String sql ="select o.id from sys_office o where (o.parent_ids like '%"+companyId+"%' or o.id ='"+companyId+"') and o.type='1' and o.del_flag ='"+Office.DEL_FLAG_NORMAL+"'";
		List<String> idsList =this.findBySql(sql);
		
		StringBuffer buffer = new StringBuffer();
		for(String id :idsList)
		{
			buffer.append("'"+id +"',");
		}
		
		String ids = buffer.length()>0?buffer.substring(0,buffer.length()-1):"";
		return ids;
	}
}
