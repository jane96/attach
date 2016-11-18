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
 * 文档权限Entity
 * 
 * @author 肖力
 * @version 2015-10-29
 */
@Entity
@Table(name = "SYS_FILES_ROLE")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FilesRole {

	private static final long serialVersionUID = 1L;
	
	
	
	
	protected String id; // 编号
	private String filesId;// 文档编号
	private String fileType;// 文档类型
	private String roleIds;// 可见权限
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

	

	public String getFilesId() {
		return filesId;
	}

	public void setFilesId(String filesId) {
		this.filesId = filesId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
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
