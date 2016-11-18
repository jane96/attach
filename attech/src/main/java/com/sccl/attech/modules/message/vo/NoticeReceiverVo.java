package com.sccl.attech.modules.message.vo;

/**
 * 通知收信人的视图对象
 * @author deng
 *
 */
public class NoticeReceiverVo {
	private String receiverName;//接收者编号 用于前台交互
	private String id;//接收者编号 用于前台交互	
	private String state; //阅读状态  '0未读 1已读'
	private String acceptingState;//接收状态 '0未接收 1已接收'
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
