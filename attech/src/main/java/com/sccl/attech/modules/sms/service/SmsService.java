/**
 * @(#)SmsService.java     1.0 13:07:44
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sms.service;

import org.apache.commons.lang.ArrayUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.utils.IdGen;
import com.sccl.attech.common.utils.JaxbUtil;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.sms.request.SmsRequest;
import com.sccl.attech.modules.sms.response.ResultInfo;
import com.sccl.attech.modules.sms.response.SmsResponse;

/**
 * The Class SmsService.
 * 
 * @author zzz
 * @version 1.0,12:57:26
 * @see com.sccl.attech.modules.sms.service
 * @since JDK1.7
 */
@Component
public class SmsService {
	/**
	 * Logger for this class
	 */
	private static final Logger	logger			= Logger.getLogger(SmsService.class);

	private final static String	SMS_KEY_USER	= "sms.service.username";
	private final static String	SMS_KEY_PWD		= "sms.service.password";
	private final static String	SMS_KEY_ADDR	= "sms.service.address";
	private final static String	SMS_KEY_METHOD	= "sms.service.method";
	private final static String	SMS_TYPE		= "sendMSM";
	public static void main(String[] args) {
		SmsService service =new SmsService();
		service.sendSms("18280390357", "外勤助手短信发送 ----3");
	}
	/**
	 * Send sms. 发送短信
	 * 
	 * @param mobiles the 手机号码,多个以,分割
	 * @return the result info
	 */
	public ResultInfo sendSms(String mobiles, String content) {
		if (logger.isDebugEnabled()) {
			logger.debug("sendSms(String=" + mobiles + ", String=" + content + ") - 开始");
		}

		SmsRequest request = initRequest(mobiles, content);
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setResult(SmsReceiver.SMS_STATUS_SUCCESS);
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(Global.getConfig(SMS_KEY_ADDR));
		try {
			if (logger.isInfoEnabled()) {
				logger.info("调用短信接口：" + Global.getConfig(SMS_KEY_ADDR) + ":" + Global.getConfig(SMS_KEY_METHOD) +",发送类容"+ request);
			}
			String requestXml=JaxbUtil.convertToXml(request);
			System.out.println(requestXml);
			Object[] result = client.invoke(Global.getConfig(SMS_KEY_METHOD), requestXml);
			if (ArrayUtils.isEmpty(result) || null == result[0]) {
				resultInfo.setResult("0");
				resultInfo.setMessage("短信接口返回值异常");

				if (logger.isDebugEnabled()) {
					logger.debug("sendSms(String=" + mobiles + ", String=" + content + ") - 结束 - 返回值=" + resultInfo);
				}
				return resultInfo;
			}
			if (logger.isInfoEnabled()) {
				logger.info("调用短信接口返回：" + Global.getConfig(SMS_KEY_ADDR) + ":" + Global.getConfig(SMS_KEY_METHOD) +",返回类容"+ result[0]);
			}
			SmsResponse response = JaxbUtil.converyToJavaBean((String) result[0], SmsResponse.class);
			resultInfo = response.getResultInfo();
		} catch (Exception e) {
			logger.error("sendSms(String=" + mobiles + ", String=" + content + ") - 异常", e);

			e.printStackTrace();
			resultInfo.setResult(SmsReceiver.SMS_STATUS_FAILE);
			resultInfo.setMessage(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("sendSms(String=" + mobiles + ", String=" + content + ") - 结束 - 返回值=" + resultInfo);
		}
		return resultInfo;
	}

	/**
	 * 初始化 request. 初始化发送数据需要的xml对象
	 * 
	 * @param mobiles the mobiles
	 * @param content the content
	 * @return the sms request
	 */
	private SmsRequest initRequest(String mobiles, String content) {
		SmsRequest request = new SmsRequest();
		request.setRequestId(IdGen.uuid()).setType(SMS_TYPE).setUserName(Global.getConfig(SMS_KEY_USER)).setPassword(Global.getConfig(SMS_KEY_PWD)).setContent(content)
				.setMobiles(mobiles);
		return request;
	}
}
