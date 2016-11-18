package com.sccl.attech.modules.message.vo;

import com.sccl.attech.modules.message.entity.SmsReceiver;


public class SmsRecordsVo {

	private String recordId;
	private String senderName;
	private String receiverName;
	private String content;
	private String sendTime;
	
	
	public SmsRecordsVo(SmsReceiver receiver) {
		this.recordId = receiver.getSmsRecords().getId();
		this.senderName = receiver.getSmsRecords().getSendName();
		this.receiverName = receiver.getReceiver().getName();
		this.content = receiver.getSmsRecords().getContent();
		this.sendTime = receiver.getSmsRecords().getSendName();
	}
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	

}
