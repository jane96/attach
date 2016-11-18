package com.sccl.attech.modules.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;

/**
 * 通知收信人员表
 * @author deng
 *
 */
@Entity
@Table(name = "notice_receiver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NoticeReceiver extends IdEntity<NoticeRecords> {
	private Office company;//公司
	private Office office; //部门
	private User receiver;//收信人
	private String state;//'0未读 1已读'
	private String projectId;//保存项目通知中的项目id，用于删除项目成员的时候使用
	private NoticeRecords noticeRecords;//通知记录
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="notice_records_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
	public NoticeRecords getNoticeRecords() {
		return noticeRecords;
	}

	public void setNoticeRecords(NoticeRecords noticeRecords) {
		this.noticeRecords = noticeRecords;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
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
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="receiver")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	

	public String getState() {
		
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Transient
	public String getStateName(){
		if("0".equals(state)){
			return "未读";
		}
		if("1".equals(state)){
			return "已读";
		}
		return state;
	}
	
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public static final String NOTICE_STATE_READ = "1";
	public static final String NOTICE_STATE_NOTREAD = "0";
	
}	
	
	

