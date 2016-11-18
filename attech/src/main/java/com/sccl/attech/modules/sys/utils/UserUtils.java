/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.google.common.collect.Maps;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.SpringContextHolder;
import com.sccl.attech.modules.sys.dao.AreaDao;
import com.sccl.attech.modules.sys.dao.CandidateDao;
import com.sccl.attech.modules.sys.dao.MenuDao;
import com.sccl.attech.modules.sys.dao.OfficeDao;
import com.sccl.attech.modules.sys.dao.RoleDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.dao.UserOfficeDao;
import com.sccl.attech.modules.sys.dao.VoteDao;
import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.Candidate;
import com.sccl.attech.modules.sys.entity.Menu;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.Role;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;
import com.sccl.attech.modules.sys.entity.Vote;
import com.sccl.attech.modules.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 用户工具类
 * @author sccl
 * @version 2013-5-29
 */
public class UserUtils extends BaseService {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static VoteDao voteDao = SpringContextHolder.getBean(VoteDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static UserOfficeDao userOfficeDao = SpringContextHolder.getBean(UserOfficeDao.class);
	private static CandidateDao candidateDao = SpringContextHolder.getBean(CandidateDao.class);

	public static final String CACHE_USER = "user";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_VOTE = "vote";
	public static final String CACHE_CANDIDATE_LIST ="candidateList";
	
	public static User getUser(){
		User user = (User)getCache(CACHE_USER);
		if (user == null){
			try{
				Subject subject = SecurityUtils.getSubject();
				Principal principal = (Principal)subject.getPrincipal();
				if (principal!=null){
					user = userDao.get(principal.getId());
					
					List<Map<String,String>> userOffices = userOfficeDao.getUserDeptList(user.getId());
					if(userOffices.size()==1)
					{
						Map<String,String> map=userOffices.get(0);
						String officeId=map.get("officeId");
						String companyId=map.get("companyId");
						
						Office office= officeDao.get(officeId);
						Office company= officeDao.get(companyId);
						
						user.setCompany(office);
						user.setOffice(company);
						
					}else if(userOffices.size()>1)
					{
						user.setCompany(null);
						user.setOffice(null);
					}
					
//					Hibernate.initialize(user.getRoleList());
					putCache(CACHE_USER, user);
				}
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		if (user == null){
			user = new User();
			try{
				SecurityUtils.getSubject().logout();
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		return user;
	}
	
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}
	
	/**
	 * 超级管理员根据机构获取角色
	 * @return
	 */
	public static List<Role> getRoleListByOrgName(String orgId){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}
	
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	public static List<Candidate> getCandidateList(){
		@SuppressWarnings("unchecked")
		List<Candidate> candidateList = (List<Candidate>)getCache(CACHE_CANDIDATE_LIST);
		if(candidateList == null)
			candidateList = candidateDao.findAllList();	
		putCache(CACHE_CANDIDATE_LIST, candidateList);
		
		return candidateList;
	}
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
//			User user = getUser();
//			if (user.isAdmin()){
				areaList = areaDao.findAllList();
//			}else{
//				areaList = areaDao.findAllChild(user.getArea().getId(), "%,"+user.getArea().getId()+",%");
//			}
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
//			if (user.isAdmin()){
//				officeList = officeDao.findAllList();
//			}else{
//				officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
//			}
			DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
			dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	
	
	public static User getUserById(String id){
		if(StringUtils.isNotBlank(id)) {
			return userDao.get(id);
		} else {
			return null;
		}
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static Map<String, Object> getCacheMap(){
		Map<String, Object> map = Maps.newHashMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			return principal!=null?principal.getCacheMap():map;
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return map;
	}
	
	
	/**
	 * 根据用户编号获取电话
	 */
	public static String getPhoneByUserId(String userIds){
		String mobile = "";
		String[] userArray = userIds.split(",");
		for (String str : userArray) {
			User user = userDao.get(str);
			String mobileStr = user.getMobile();
			mobile = mobile+mobileStr+",";
		}
		String mobiles = mobile.substring(0,mobile.length()-1);
		return mobiles;
	}
	
}
