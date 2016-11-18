/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.message.entity.SmsRecords;
import com.sccl.attech.modules.message.entity.SmsTariff;
import com.sccl.attech.modules.sys.utils.DictUtils;

/**
 * 机构Entity
 * @author sccl
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_office")
@DynamicInsert @DynamicUpdate
public class Office extends IdEntity<Office> {

	private static final long serialVersionUID = 1L;
	private Office parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private Area area;		// 归属区域
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private String shortName;//名称简称
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	
	private String industry;//所属行业
	private Integer sort;	//排序号  
	private Integer level;	//节点深度
	
	private String outId; //外部ID
	
	private List<User> userList = Lists.newArrayList();   // 拥有用户列表
	private List<SmsRecords> recordsList = Lists.newArrayList();//公司下面的  发送短信记录
	private List<SmsRecords> smsRecords = Lists.newArrayList();//部门下的  发送短信记录
	private List<SmsReceiver> receiversList = Lists.newArrayList(); //公司下面的  接收短信记录
	private List<SmsReceiver> smsReceivers = Lists.newArrayList();  //部门下面的  接收短信记录
	private List<SmsTariff> smsTariffs = Lists.newArrayList();//公司所拥有的短信
	
	private String typeName; //机构类型名称
	private String parentId; // 父Id
	private String parentName;
	private String areaId; //区域id
	private String areaName; //区域名称
	private String label;
	
	private String roleIds; //负责人编号
	private String roleNames;//负责人姓名
	
	private Dict dict;//职位
	private String companyId;//企业Id
	private String roleType;//角色
	
	@Transient
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Transient
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Transient
	@JsonIgnore
	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	private List<Office> children;//孩子部门集合
	public Office(){
		super();
	}
	
	public Office(String id){
		this();
		this.id = id;
	}
	
	

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="上级部门", align=2, sort=30)
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@ManyToOne
	@JoinColumn(name="area_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="归属区域", align=2, sort=35)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min=1, max=100)
	@ExcelField(title="机构名称", align=2, sort=40)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=1)
	@ExcelField(title="机构类型", align=2, sort=50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	@ExcelField(title="联系电话", align=2, sort=50)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	@Length(min=0, max=100)
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Length(min=0, max=100)
	@ExcelField(title="机构编码", align=2, sort=45)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@OneToMany(mappedBy = "office", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}


	
	@OneToMany(mappedBy = "company", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsRecords> getRecordsList() {
		return recordsList;
	}

	public void setRecordsList(List<SmsRecords> recordsList) {
		this.recordsList = recordsList;
	}

	@OneToMany(mappedBy = "office", fetch=FetchType.LAZY)
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
	
	
	@OneToMany(mappedBy = "company", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsReceiver> getReceiversList() {
		return receiversList;
	}

	public void setReceiversList(List<SmsReceiver> receiversList) {
		this.receiversList = receiversList;
	}

	@OneToMany(mappedBy = "office", fetch=FetchType.LAZY)
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

	@OneToMany(mappedBy = "company", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsTariff> getSmsTariffs() {
		return smsTariffs;
	}

	public void setSmsTariffs(List<SmsTariff> smsTariffs) {
		this.smsTariffs = smsTariffs;
	}

	@Transient
	public static void sortList(List<Office> list, List<Office> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Office e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Office child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	@Length(min=0, max=100)
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name="\"level\"")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}

	@Transient
	public String getTypeName() {
		return DictUtils.getDictLabel(type, "sys_office_type", "无");
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Transient
	public String getParentId() {
		if(StringUtils.isBlank(this.parentId)){
			if(parent!=null){
				return parent.getId();
			}
		}
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Transient
	public String getParentName() {
		if(StringUtils.isBlank(this.parentName)){
			if(parent!=null){
				return parent.getName();
			}
		}
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Transient
	public String getAreaId() {
		if(StringUtils.isBlank(this.areaId)){
			if(area!=null){
				return area.getId();
			}
		}
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	@Transient
	public String getAreaName() {
		if(StringUtils.isBlank(this.areaName)){
			if(area!=null){
				return area.getName();
			}
		}
		return this.areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}


	@Transient
	public String getLabel() {
		return shortName;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String hasChildren;

	@Transient
	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Office> getChildren() {
		return children;
	}

	public void setChildren(List<Office> children) {
		this.children = children;
	}
	
	@Transient
	public String getRoleIds() {
		if(StringUtils.isEmpty(roleIds)){
			roleIds = master;
		}
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	@Transient
	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
}