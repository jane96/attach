/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.Collections3;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.common.utils.excel.fieldtype.RoleListType;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.message.entity.SmsRecords;

/**
 * 用户Entity
 * @author sccl
 * @version 2013-5-15
 */
@Entity
@Table(name = "sys_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属公司
	private Office office;	// 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型 (0:企业管理员,1:部门管理员,2:外勤人员)
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	
	private Integer locationOn; //激活定位（0激活定位 1取消定位）
	private Integer state; // 用户状态（0：正式用户；1：试用用户）
	private Date expirationTime; //到期时间
	private String photo; //头像
	
	private String areaSetupId;	//企业考勤区域设置表编号（多个考勤区域逗号分隔） 关联的是 attendance_area_setup表，不是sys_area表
	private String timeSetupId;	//企业考勤时间设置表编号（多个考勤时间逗号分隔）
	
	private String outId; //外部ID
	
	private String companyName;
	private String officeName;
	private String officeId2;
	private String companyId2;
	private String roleIds;
	private String officeId;
	private Double xaxis; //X轴
	private Double yaxis; //y轴
	private String locationDesc; //位置描述
	private Date locationDate; //定位时间
	
	private List<SmsRecords> smsRecords = Lists.newArrayList();//所有的短信记录
	private List<SmsReceiver> smsReceivers = Lists.newArrayList();//收到的短信
	
	private List<Role> newRoleList;
	private List<Office> officeList;
	private String userOfficeId;
	
	@Transient
	public String getUserOfficeId() {
		return userOfficeId;
	}

	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}

	@Transient
	@JsonIgnore
	public List<Office> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}

	@Transient
	@JsonIgnore
	public List<Role> getNewRoleList() {
		return newRoleList;
	}

	public void setNewRoleList(List<Role> newRoleList) {
		this.newRoleList = newRoleList;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	@Transient
	public String getOfficeId2() {
		return officeId2;
	}

	public void setOfficeId2(String officeId2) {
		this.officeId2 = officeId2;
	}
	@Transient
	public String getCompanyId2() {
		return companyId2;
	}

	public void setCompanyId2(String companyId2) {
		this.companyId2 = companyId2;
	}

	@Transient
	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getLocationDate() {
		return locationDate;
	}

	public void setLocationDate(Date locationDate) {
		this.locationDate = locationDate;
	}

	@Transient
	public Double getXaxis() {
		return xaxis;
	}

	public void setXaxis(Double xaxis) {
		this.xaxis = xaxis;
	}
	@Transient
	public Double getYaxis() {
		return yaxis;
	}

	public void setYaxis(Double yaxis) {
		this.yaxis = yaxis;
	}

	@Transient
	 public String getRoleIds() {
		List<String> listStrings = getRoleIdList();
		StringBuilder sb = new StringBuilder();
		for(String s : listStrings){
			sb.append(s).append(",");
		}
		if(sb != null && sb.length() > 0){
			
			return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
		}
		return null;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	@Transient
	public String getOfficeId() {
		if(office!=null)
		{
			return office.getId();
		}
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}


	@Transient
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	public User() {
		super();
	}
	
	public User(String id) {
		this();
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="归属公司不能为空")
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
	///@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100)
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100)
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=1, max=100)
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email @Length(min=0, max=200)
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200)
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    @Length(min=0, max=200)
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Transient
	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100)
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Transient
	@ExcelField(title="创建时间", type=0, align=1, sort=90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	@Transient
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}
	
	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(String id){
		return id != null && id.equals("1");
	}
	public Integer getLocationOn() {
		return locationOn;
	}

	public void setLocationOn(Integer locationOn) {
		this.locationOn = locationOn;
	}
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	@OneToMany(mappedBy = "sender", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsRecords> getSmsRecords() {
		return smsRecords;
	}

	public void setSmsRecords(List<SmsRecords> smsRecords) {
		this.smsRecords = smsRecords;
	}
	
	@OneToMany(mappedBy = "receiver", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsReceiver> getSmsReceivers() {
		return smsReceivers;
	}

	public void setSmsReceivers(List<SmsReceiver> smsReceivers) {
		this.smsReceivers = smsReceivers;
	}
	

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAreaSetupId() {
		return areaSetupId;
	}

	public void setAreaSetupId(String areaSetupId) {
		this.areaSetupId = areaSetupId;
	}

	public String getTimeSetupId() {
		return timeSetupId;
	}

	public void setTimeSetupId(String timeSetupId) {
		this.timeSetupId = timeSetupId;
	}
	
	
	
	
	
//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
}