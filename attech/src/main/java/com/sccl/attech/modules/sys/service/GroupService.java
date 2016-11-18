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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.CacheUtils;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StrUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.modules.sys.dao.DictDao;
import com.sccl.attech.modules.sys.dao.GroupDao;
import com.sccl.attech.modules.sys.dao.GroupUserDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Grouping;
import com.sccl.attech.modules.sys.entity.GroupingUsers;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.DictUtils;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 群组Service
 * @author 肖力
 * @version 2015-10-1
 */
@Service
@Transactional(readOnly = true)
public class GroupService extends BaseService {

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupUserDao groupUserDao;
	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private MyBatisDictDao myBatisDictDao;
	
	public Grouping get(String id) {
		// MyBatis 查询
//		return myBatisDictDao.get(id);
		// Hibernate 查询
		return groupDao.get(id);
	}
	
	public Page<Grouping> findGroup(Page<Grouping> page, Grouping group) {
		
		DetachedCriteria dc = groupDao.createDetachedCriteria();
		dc.add(Restrictions.sqlRestriction("del_flag=0"));
		return groupDao.find(page, dc);
	}
	
	public User findUser(String id) {
		List list=null;
		if(null!=id&&!"".equals(id)){
			list=userDao.find("from User where id='"+id+"'");
		}
		
		
		return (User)list.get(0);
	}
	
	public Grouping findGroupUser(String id) {
		
		List<GroupingUsers> list=groupDao.find("from GroupingUsers where groupingId='"+id+"'");
		String userNames="";
		String ids="";
		for(int a=0;a<list.size();a++){
			
			List<User> userList=userDao.find("from User where id ='"+list.get(a).getUserId()+"'");
			userNames+=userList.get(0).getName()+",";
			ids+=userList.get(0).getId()+",";
		}
		Grouping group=new Grouping();
		group.setGroupUsers(userNames);
		group.setGroupUsersId(ids);
		return group;
	}
	
	public String findGroupUserIds(String id) {
		
		List<GroupingUsers> list=groupDao.find("from GroupingUsers where groupingId='"+id+"'");
		String ids="";
		for(int a=0;a<list.size();a++){
			
			List<User> userList=userDao.find("from User where id ='"+list.get(a).getUserId()+"'");
			ids+=userList.get(0).getIds();
		}
		
		return ids;
	}
	
	@Transactional(readOnly = false)
	public String save(Grouping group) {
		String id=groupDao.saveRet(group);
		
		return id;
	}
	
	@Transactional(readOnly = false)
	public void saveGroupUser(GroupingUsers group) {
		groupUserDao.save(group);
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		groupDao.deleteById(id);
		
	}
	
	@Transactional(readOnly = false)
	public void deleteGroupUser(String groupId) {
		Parameter parameter=new Parameter(groupId);
		groupUserDao.updateBySql("delete from sys_grouping_users where grouping_id=:p1", parameter);
		
	}
	
	@Transactional(readOnly = false)
	public void updateGroup(String id,Grouping group) {
		int sort=0;
		if(null!=group.getSort()){
			sort=group.getSort();
		}else{
			sort=0;
		}
		String type="";
		if(null!=group.getType()&&!"".equals(group.getType())){
			type=group.getType();
		}else{
			type="0";
		}
		Parameter parameter=new Parameter(group.getName(),type,sort,group.getUpdateBy(),id);
		groupUserDao.updateBySql("update sys_grouping set name=:p1,type=:p2,sort=:p3,update_date=sysdate,update_by=:p4 where id=:p5",parameter);
		
	}
}
