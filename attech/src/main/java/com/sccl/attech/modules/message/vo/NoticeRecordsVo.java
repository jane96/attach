package com.sccl.attech.modules.message.vo;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sccl.attech.modules.message.entity.NoticeRecords;

/**
 * 和前台交互的视图模型
 * @author deng
 *
 */
public class NoticeRecordsVo {
	private String id;//记录编号
	private Date sendTime;//发送时间
	private String title;//通知标题
	private String content;//通知内容
	private Integer sort;	// 排序
	
	private String officeId;//归属机构id用于前后台交互 
	private String companyId;//公司编号id 用于前台交互
	private String senderName;//发送者姓名 用于前台交互
	private String senderId;//发送者编号 用于前台交互
	
	//----通知收信人员表
	private String receiverName;//接收者编号 用于前台交互
	private String receiverId;//接收者编号 用于前台交互	
	private String state; //阅读状态  '0未读 1已读'
	private String acceptingState;//接收状态 '0未接收 1已接收'
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAcceptingState() {
		return acceptingState;
	}
	public void setAcceptingState(String acceptingState) {
		this.acceptingState = acceptingState;
	}
	
	
}
