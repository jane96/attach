/**
 * Sunnysoft.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.sccl.attech.common.vo;

import com.sccl.attech.common.mapper.JsonMapper;


public class ResultData implements java.io.Serializable {

	/** 序列化号 */
	private static final long	serialVersionUID	= -1871002514376173829L;
	private boolean				success;
	/**
	 * 返回的消息
	 */
	private String				message;
	
	private String             state;
	/**
	 * 返回的值，通过json对象传输
	 */
	private Object				data;

	public ResultData() {
	}

	public ResultData(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public ResultData(String message) {
		this.success = true;
		this.message = message;
	}

	public ResultData(boolean success, String message, Object object) {
		super();
		this.success = success;
		this.message = message;
		this.data = object;
	}

	public ResultData(Object object) {
		super();
		this.data=object;
		//this.data = getJsonStringFromObject(object);
	}

	/**
	 * Getter method for property <tt>message</tt>.
	 * 
	 * @return property value of message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter method for property <tt>message</tt>.
	 * 
	 * @param message value to be assigned to property message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/***
	 * 根据对象，将对象转换为jsonString
	 * 
	 * @param object
	 * @return
	 */
	private String JsonStringFromObject(Object object) {
		if (object == null) {
			return null;
		}
		return JsonMapper.getInstance().toJson(object);
	}
//
	public String Json() {
		return JsonStringFromObject(this);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
