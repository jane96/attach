package com.sccl.attech.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.sccl.attech.common.utils.IdGen;

/**
 * 群组管理用户关联Entity
 * @author 肖力
 * @version 2015-10-1
 */
@Entity
@Table(name = "sys_grouping_users")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GroupingUsers {

	private static final long serialVersionUID = 1L;

	protected String id;		// 编号
	private String groupingId;//分组主表ID
	private String userId;//对应的用户id
	
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}

	@Id
	//@Length(min=1, max=64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupingId() {
		return groupingId;
	}

	public void setGroupingId(String groupingId) {
		this.groupingId = groupingId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
