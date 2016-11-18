package com.sccl.attech.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;

@Entity
@Table(name = "sys_user_office")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserOffice extends IdEntity<UserOffice> {
	private User user;//用户
	private Office dept;//部门
	private Office company;//企业
	private Dict role;//角色
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="用户")
	@ExcelField(title="归属的用户", align=2, sort=20)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=20)
	public Office getDept() {
		return dept;
	}
	public void setDept(Office dept) {
		this.dept = dept;
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
	@JoinColumn(name="role_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	//@NotNull(message="角色不能为空")
	@ExcelField(title="角色", align=2, sort=20)
	public Dict getRole() {
		return role;
	}
	public void setRole(Dict role) {
		this.role = role;
	}
	
}
