/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.common.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * Entity支持类
 * @author sccl
 * @version 2013-01-15
 */
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前用户
	 */
	protected User currentUser;
	
	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 *从页面搜索还是从菜单搜索（用于非页面搜索时设置默认搜索条件）
	 */
	private boolean searchFromPage;

	/**
	 *用于搜索多个ID的时候设置搜索条件
	 */
	private String ids;
	
	@JsonIgnore
	@XmlTransient
	@Transient
	public User getCurrentUser() {
		if(currentUser == null){
			currentUser = UserUtils.getUser();
		}
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@JsonIgnore
	@XmlTransient
	@Transient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	@Transient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	@Transient
	public boolean isSearchFromPage() {
		return searchFromPage;
	}

	@Transient
	public void setSearchFromPage(boolean searchFromPage) {
		this.searchFromPage = searchFromPage;
	}

	@Transient
	public String getIds() {
		return ids;
	}

	@Transient
	public void setIds(String ids) {
		this.ids = ids;
	}

	
	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";
	
	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String FIELD_DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	//类型标记
	public static final int IS_END_NO = 1;
	public static final int IS_END_YES = 2;
	public static final String SEL_ISEND ="isEnd";
	public static final String SEL_DEADLINE = "deadline";
	public static final Date SEl_DATE = new Date(); 
	//多对多的表
	public static final String USER_VOTE_USER_ID = "userId";
	public static final String VOTE_ID = "id";
	/**
	 * 公有群组
	 */
	public static final String PUBLIC_GROUP="1";
	
	/**
	 * 私有群组
	 */
	public static final String PRIVATE_GROUP="0";
	
}
