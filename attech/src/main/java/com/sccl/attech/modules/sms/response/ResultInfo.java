/**
 * @(#)ResultInfo.java     1.0 12:59:21
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sms.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class ResultInfo. 发送短信，返回值
 * 
 * @author zzz
 * @version 1.0,12:59:21
 * @see com.sccl.attech.modules.sms.response
 * @since JDK1.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultInfo {

	// 1：失败0：成功
	/** The 调用接口是否成功的标识. */
	@XmlElement(required = true)
	private String	result;//(1.失败，0.成功)
	/** The 结果说明. */
	@XmlElement
	private String	message; //返回接口调用是否成功的汉字描述

	/**
	 * 获取 the 调用接口是否成功的标识.
	 * 
	 * @return the 调用接口是否成功的标识
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置 the 调用接口是否成功的标识.
	 * 
	 * @param result the new 调用接口是否成功的标识
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 获取 the 结果说明.
	 * 
	 * @return the 结果说明
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置 the 结果说明.
	 * 
	 * @param message the new 结果说明
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("message", this.message).append("result", this.result).toString();
	}
}
