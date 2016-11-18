/**
 * @(#)SmsResponse.java     1.0 12:41:47
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sms.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class SmsResponse.
 * 
 * @author zzz
 * @version 1.0,12:41:47
 * @see com.sccl.attech.modules.sms.response
 * @since JDK1.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public class SmsResponse {

	/** The 请求ID. */
	@XmlElement(required = true)
	private String	requestId;

	/** The 调用接口的返回值. */
	@XmlElement(name="ResultInfo",required = true)
	private ResultInfo resultInfo;


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
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ResultInfo getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("resultInfo", this.resultInfo).append("requestId", this.requestId).toString();
	}

}
