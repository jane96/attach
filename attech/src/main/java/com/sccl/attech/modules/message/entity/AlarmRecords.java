/**
 * @(#)AlarmRecords.java     1.0 14:34:51
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.message.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 告警记录
 * @author deng
 *
 */
@Entity
@Table(name = "alarm_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AlarmRecords extends IdEntity<AlarmRecords> {
	private static final long	serialVersionUID	= -1787044004512094684L;
	
	/** The state 未读. */
	public static String		STATE_NOREAD		= "0";

	/** The state 已读. */
	private static String		STATE_READ			= "1";
	private Office company; //归属公司
	private Office office; //归属部门
	private User user;    //外勤人员
	private String name;//外勤人员姓名
	private String type; //告警类型（0：围栏告警；1：账号过期；）
	private String content;//告警内容
	private String state;//是否已读（0：未读，1：已读）
	private String location_desc;//告警发生地
	private Date location_date;//告警发生时间
	private String typeName;//导出excel显示
	
	public final static String ALARM_RECORD_TYPE="0";
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
	@ExcelField(title="所在公司", align=2, sort=30)
	public Office getCompany() {
		return company;
	}
	public void setCompany(Office company) {
		this.company = company;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="offiec_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
	@ExcelField(title="所在部门", align=2, sort=30)
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ExcelField(title="外勤人员", align=2, sort=30)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Transient
	@ExcelField(title="告警类型", align=2, sort=40)
	public String getTypeName() {
		if("1".equals(type)){
		   typeName = "账号过期";
		}else if("0".equals(type)){
			typeName = "围栏告警";
		}
		return typeName;
	}
	
	@ExcelField(title="告警类容", align=2, sort=50)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@ExcelField(title="告警描述", align=2, sort=30)
	public String getLocation_desc() {
		return location_desc;
	}
	public void setLocation_desc(String location_desc) {
		this.location_desc = location_desc;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="告警时间", align=2, sort=40)
	public Date getLocation_date() {
		return location_date;
	}
	public void setLocation_date(Date location_date) {
		this.location_date = location_date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public void initByUser(User user){
		this.name=user.getName();
		this.company=user.getCompany();
		this.office=user.getOffice();
		this.state=AlarmRecords.STATE_NOREAD;//默认未读
		this.location_date=new Date();
	}
	public static final String ALARM_STATE_READ = "1";
	public static final String ALARM_STATE_NOTREAD = "0";
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.id).append("typeName", this.typeName).append("content", this.content)
				.append("office", this.office).append("name", this.name).append("company", this.company).append("state", this.state).append("location_date", this.location_date)
				.append("location_desc", this.location_desc).append("type", this.type).append("user", this.user).toString();
	}
}
