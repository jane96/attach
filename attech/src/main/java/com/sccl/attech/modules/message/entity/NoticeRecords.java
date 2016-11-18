package com.sccl.attech.modules.message.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;

/**
 * 通知记录
 * @author deng
 *
 */
@Entity
@Table(name = "notice_records")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NoticeRecords extends IdEntity<NoticeRecords> {
	private static final long serialVersionUID = 1L;
	
	private Office company;//公司编号
	private Office office; // 部门编号
	private User sender;//发信人
	private Date sendTime;//发送时间
	private String title;//通知标题
	private String content;//通知内容
	private Integer sort;	// 排序
	
	
	
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
	@JoinColumn(name="sender")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	
}	    
