/**
 * @(#)SmsRequest.java     1.0 12:41:51
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sms.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class SmsRequest.
 * 
 * @author zzz
 * @version 1.0,12:41:51
 * @see com.sccl.attech.modules.sms.request
 * @since JDK1.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SmsRequest {

	/** The 请求ID. */
	@XmlElement(required = true)
	private String	requestId;

	/** The 类型. */
	@XmlElement(required = true)
	private String	type;

	/** The 用户姓名. */
	@XmlElement(required = true)
	private String	userName;

	/** The 系统分配的密码. */
	@XmlElement(required = true)
	private String	password;

	/** The 接收短信的手机号码. */
	@XmlElement(required = true)
	private String	mobiles;

	/** The 短信内容. */
	@XmlElement(required = true)
	private String	content;

	/**
	 * 获取 the 请求ID.
	 * 
	 * @return the 请求ID
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * 设置 the 请求ID.
	 * 
	 * @param requestId the new 请求ID
	 */
	public SmsRequest setRequestId(String requestId) {
		this.requestId = requestId;
		return this;
	}

	/**
	 * 获取 the 类型.
	 * 
	 * @return the 类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置 the 类型.
	 * 
	 * @param type the new 类型
	 */
	public SmsRequest setType(String type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取 the 用户姓名.
	 * 
	 * @return the 用户姓名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置 the 用户姓名.
	 * 
	 * @param userName the new 用户姓名
	 */
	public SmsRequest setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	 * 获取 the 系统分配的密码.
	 * 
	 * @return the 系统分配的密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置 the 系统分配的密码.
	 * 
	 * @param password the new 系统分配的密码
	 */
	public SmsRequest setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * 获取 the 接收短信的手机号码.
	 * 
	 * @return the 接收短信的手机号码
	 */
	public String getMobiles() {
		return mobiles;
	}

	/**
	 * 设置 the 接收短信的手机号码.
	 * 
	 * @param mobiles the new 接收短信的手机号码
	 */
	public SmsRequest setMobiles(String mobiles) {
		this.mobiles = mobiles;
		return this;
	}

	/**
	 * 获取 the 短信内容.
	 * 
	 * @return the 短信内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置 the 短信内容.
	 * 
	 * @param content the new 短信内容
	 */
	public SmsRequest setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("type", this.type).append("content", this.content).append("mobiles", this.mobiles)
				.append("requestId", this.requestId).append("password", this.password).append("userName", this.userName).toString();
	}

}
