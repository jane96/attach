package com.sccl.attech.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

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
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
@Entity
@Table(name = "sys_vote_candidate")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Candidate extends IdEntity<Candidate>{
	private static final long serialVersionUID = 1L;
	private String candidate;
	private int num;
	private String candidateIntroduce;
	private Vote vote;
	private float status;
	public Candidate(){}
	public Candidate(String candidate, int num, String candidateIntroduce, Vote vote, float status) {
		super();
		this.candidate = candidate;
		this.num = num;
		this.candidateIntroduce = candidateIntroduce;
		this.vote = vote;
		this.status = status;
	}

	@ExcelField(title="状态", align=2, sort=45)
	public float getStatus() {
		return status;
	}
	
	public void setStatus(float status) {
		this.status = status;
	}

	@Length(min=1, max=100)
	@ExcelField(title="选项", align=2, sort=45)
	public String getCandidate() {
		return candidate;
	}
	
	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}
	@ExcelField(title="投票数", align=2, sort=45)
	public int getNum() {
		return num;
	}
	
	
	public void setNum(int num) {
		this.num = num;
	}
	@Length(min=1, max=100)
	@ExcelField(title="选项描述", align=2, sort=45)
	public String getCandidateIntroduce() {
		return candidateIntroduce;
	}
	
	public void setCandidateIntroduce(String candidateIntroduce) {
		this.candidateIntroduce = candidateIntroduce;
	}
	@ManyToOne
	@JoinColumn(name="vote_id")
	@NotFound(action = NotFoundAction.IGNORE)
	//@JsonIgnore
	///@NotNull(message="归属部门不能为空")
	@ExcelField(title="投票标题", align=2, sort=25)
	public Vote getVote() {
		return vote;
	}
	
	public void setVote(Vote vote) {
		this.vote = vote;
	}
}
