/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sccl.attech.common.persistence.BaseEntity;
import com.sccl.attech.common.utils.CustomDateDetailSerializer;
import com.sccl.attech.common.utils.IdGen;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;

/**
 * 日志Entity
 * @author sccl
 * @version 2013-05-30
 */
@Entity
@Table(name = "sys_log")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Log extends BaseEntity<Log> {

	private static final long serialVersionUID = 1L;
	private String id;			// 日志编号
	private String type; 		// 日志类型（1：接入日志；2：异常日志；3：编辑日志；4：修改日志；5：删除日志；6：登录日志；7：登出日志；8：导出日志；9：权限配置；）
	private User createBy;		// 创建者
	private Date createDate;	// 日志创建时间
	private String remoteAddr; 	// 操作用户的IP地址
	private String requestUri; 	// 操作的URI
	private String method; 		// 操作的方式
	private String params; 		// 操作提交的数据
	private String userAgent;	// 操作用户代理信息
	private String exception; 	// 异常信息
	private Office company;    // 公司
	private Office office;     //部门
	private String companyName;

	private String officeName;
	
	private String operationName;
	
	private String companyId;//公司id，高级查询使用
	
	private Date start;
	private Date end;
	
	@Transient
	public String getOperationName() {
		if(createBy != null){
			return createBy.getName();
		}
		return null;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public static final String TYPE_ACCESS = "1";//接入日志
	public static final String TYPE_EXCEPTION = "2";//异常日志
	public static final String TYPE_UPDATEORADD = "3";//编辑日志 （修改或添加）
	public static final String TYPE_UPDATE = "4";//特殊修改日志
	public static final String TYPE_DELETE = "5";//删除日志
	public static final String TYPE_LOGIN = "6";//登录日志
	public static final String TYPE_LOGINOUT = "7";//登出日志
	public static final String TYPE_EXPOT = "8";//导出日志
	public static final String TYPE_AUTHORITY = "9";//权限配置
	public static final String TYPE_SGACCESS = "0";//省管接口日志
	 @Transient
	public String getOfficeName() {
		 if(office==null){
			 return null;
		 }
		 else{
			 return office.getName();
		 }
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	 @Transient
	public String getCompanyName() {
		 if(company==null){
			 return null;
		 }
		 else{
			 return company.getName();
		 }
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	@Transient
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Log(){
		super();
	}
	
	public Log(String id){
		this();
		this.id = id;
	}

	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
	
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using=CustomDateDetailSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	@Transient
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	@Transient
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="归属公司", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="归属部门", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
}