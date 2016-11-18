package com.sccl.attech.modules.sys.entity;

import java.util.Date;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.common.utils.excel.fieldtype.RoleListType;
@Entity
@Table(name = "sys_votes")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vote extends IdEntity<Vote>{
	private static final long serialVersionUID = 1L;
	private String votingContent;
	private List<User> user;
	private Integer isEnd;
	private Date deadline;
	private String flag;
	private int num;
	private List<String> candidate;
	private int selectNum;
	//1.代表单选2.代表多选
	private int selectType;
	public Vote(){
		super();
	}
	public Vote(String id){
		this();
		this.id = id;
	}
	
	public Vote(String votingContent, List<User> user, Integer isEnd, Date deadline, String flag, int num,
			List<String> candidate, int selectType,int selectNum) {
		super();
		this.votingContent = votingContent;
		this.user = user;
		this.isEnd = isEnd;
		this.deadline = deadline;
		this.flag = flag;
		this.num = num;
		this.candidate = candidate;
		this.selectType = selectType;
		this.selectNum = selectNum;
	}
	public int getSelectType() {
		return selectType;
	}
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}
	@Transient
	public List<String> getCandidate() {
		return candidate;
	}

	public void setCandidate(List<String> candidate) {
		this.candidate = candidate;
	}
	@ExcelField(title="标志", align=2, sort=45)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@ExcelField(title="是否结束", align=2, sort=45)
	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	@Length(min=1, max=100)
	@ExcelField(title="投票标题", align=2, sort=45)
	public String getVotingContent() {
		return votingContent;
	}
	
	public void setVotingContent(String votingContent) {
		this.votingContent = votingContent;
	}
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_vote", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "vote_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	@ExcelField(title="投票的用户", align=1, sort=800, fieldType=RoleListType.class)
	public List<User> getUser() {
		return user;
	}
	
	public void setUser(List<User> user) {
		this.user = user;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="截至日期", type=1, align=1, sort=110)
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	@ExcelField(title="投票总数", type=1, align=1, sort=110)
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Column(name="select_num")
	@ExcelField(title="选项数量", type=1, align=1, sort=110)
	public int getSelectNum() {
		return selectNum;
	}
	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}
	
	
	
	
	
}
