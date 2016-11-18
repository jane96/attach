/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 用户DAO接口
 * @author sccl
 * @version 2013-8-23
 */
@Repository
public class UserDao extends BaseDao<User> {
	
	public List<User> findAllList() {
		return find("from User where delFlag=:p1 order by id", new Parameter(User.DEL_FLAG_NORMAL));
	}
	
	public User findByLoginName(String loginName){
		return getByHql("from User where loginName = :p1 and delFlag = :p2", new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}

	public List<User> findByNames(String names){
		return find("from User where name = :p1 and delFlag = :p2", new Parameter(names, User.DEL_FLAG_NORMAL));
	}
	
	public int updatePasswordById(String newPassword, String id){
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}
	
	public int updateLoginInfo(String loginIp, Date loginDate, String id){
		return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3", new Parameter(loginIp, loginDate, id));
	}
	
	public int merageUser(User user){
		getSession().merge(user);
		return  1;
	}
	
	
	/**
	 * 根据outId获取用户
	 * @param outId
	 * @return
	 */
	public User findByOutId(String outId){
		User user = new User();
		List<Object> find = find("from User where outId = :p1 and delFlag = :p2", new Parameter(outId, User.DEL_FLAG_NORMAL));
		if(find!=null&&find.size()>0){
			user = (User)find.get(0);
		}
		return user;
	}
	
}
