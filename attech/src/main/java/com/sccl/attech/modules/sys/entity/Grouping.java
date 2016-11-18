package com.sccl.attech.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.IdGen;

/**
 * 群组管理Entity
 * 
 * @author 肖力
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_grouping")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Grouping {

	private static final long serialVersionUID = 1L;
	
	
	
	
	protected String id; // 编号
	private String companyId;// 公司编号
	private String offiecId;// 部门编号
	private String name;// 群组名称
	private String type;// 群组类型（0-私有；1-公有）
	private Integer sort;// 序号
	private String createBy;// 创建者
	private Date createDate;// 创建时间
	private String updateBy;// 更新者
	private Date updateDate;// 更新时间
	private String remarks;// 备注信息
	private String delFlag;// 删除标记

	//--------------和数据库无关的数据-------------------------
	private String typeName;// 群组类型名称
	private String createDate_format;// 转换之后的时间类型
	private String updateDate_format;// 转换之后的时间类型
	private String createName;//创建者名字
	private String groupUsers;
	private String groupUsersId;
	//--------------------------------------------------
	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@Id
	// @Length(min=1, max=64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOffiecId() {
		return offiecId;
	}

	public void setOffiecId(String offiecId) {
		this.offiecId = offiecId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Transient
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Transient
	public String getCreateDate_format() {
		return createDate_format;
	}

	public void setCreateDate_format(String createDate_format) {
		this.createDate_format = createDate_format;
	}

	@Transient
	public String getUpdateDate_format() {
		return updateDate_format;
	}

	public void setUpdateDate_format(String updateDate_format) {
		this.updateDate_format = updateDate_format;
	}

	@Transient
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Transient
	public String getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(String groupUsers) {
		this.groupUsers = groupUsers;
	}

	@Transient
	public String getGroupUsersId() {
		return groupUsersId;
	}

	public void setGroupUsersId(String groupUsersId) {
		this.groupUsersId = groupUsersId;
	}

	
	
}
