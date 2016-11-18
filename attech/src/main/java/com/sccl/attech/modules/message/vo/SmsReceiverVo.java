package com.sccl.attech.modules.message.vo;



public class SmsReceiverVo {

	private String Id;	//记录Id	
	private String recordsId;	//短信记录Id
	private String sendTime; 	// 发送时间
	private String basedataType; // 短信分类(关联sys_basedata主键编号)
	private String content; 	// 短信内容
	
	private String sendId;		//发送人Id
	private String sendName;	//发送人姓名
	private String sendPhone;	//发送人电话
	
	private String receiverId;	//收信人Id
	private String receiverName; //收信人姓名 
	private String receiverPhone; //收信人电话
	
	private String status;	//0未读 1已读

	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getRecordsId() {
		return recordsId;
	}

	public void setRecordsId(String recordsId) {
		this.recordsId = recordsId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getBasedataType() {
		return basedataType;
	}

	public void setBasedataType(String basedataType) {
		this.basedataType = basedataType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendPhone() {
		return sendPhone;
	}

	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
