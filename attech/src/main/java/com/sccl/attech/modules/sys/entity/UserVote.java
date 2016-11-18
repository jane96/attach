package com.sccl.attech.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.sccl.attech.common.persistence.IdEntity;
@Entity
@Table(name = "sys_user_vote")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserVote extends IdEntity<UserVote>{
	private String userId;
	private String voteId;
	public UserVote(){}
	public UserVote(String userId, String voteId) {
		super();
		this.userId = userId;
		this.voteId = voteId;
	}
	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="vote_id")
	public String getVoteId() {
		return voteId;
	}
	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}
	
	
}
