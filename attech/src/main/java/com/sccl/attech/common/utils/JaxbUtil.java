/**
 * @(#)JaxbUtil.java     1.0 13:01:52
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.common.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sccl.attech.modules.sms.request.SmsRequest;
import com.sccl.attech.modules.sms.response.ResultInfo;
import com.sccl.attech.modules.sms.response.SmsResponse;

/**
 * The Class JaxbUtil.
 * jaxb 工具类， 用于obj->xml
 * @author zzz
 * @version 1.0,13:01:52
 * @see com.sccl.attech.common.utils
 * @since JDK1.7
 */
public class JaxbUtil {
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws JAXBException the jAXB exception
	 */
	public static void main(String[] args) throws JAXBException {
		SmsRequest request = new SmsRequest();
		request.setRequestId(System.nanoTime() + "");
		request.setType("0");
		String result = JaxbUtil.convertToXml(request);
		
		System.out.println(result);

		SmsResponse response = new SmsResponse();
		response.setRequestId("200");
		ResultInfo resultInfo=new ResultInfo();
		resultInfo.setResult("十户联防");
		resultInfo.setMessage(System.nanoTime() + "");
		response.setResultInfo(resultInfo);
		
		result = JaxbUtil.convertToXml(response);
		
		System.out.println(result);
	}

	/**
	 * JavaBean转换成xml 默认编码UTF-8.
	 * 
	 * @param obj the obj
	 * @return the string
	 * @throws JAXBException the jAXB exception
	 */
	public static String convertToXml(Object obj) throws JAXBException {
		return convertToXml(obj, "UTF-8");
	}

	/**
	 * JavaBean转换成xml.
	 * 
	 * @param obj the obj
	 * @param encoding the encoding
	 * @return the string
	 * @throws JAXBException the jAXB exception
	 */
	public static String convertToXml(Object obj, String encoding) throws JAXBException {
		String result = null;
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

		StringWriter writer = new StringWriter();
		marshaller.marshal(obj, writer);
		result = writer.toString();
		return result;
	}

	/**
	 * xml转换成JavaBean.
	 * 
	 * @param <T> the generic type
	 * @param xml the xml
	 * @param c the c
	 * @return the t
	 * @throws JAXBException the jAXB exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(String xml, Class<T> c) throws JAXBException {
		T t = null;
		JAXBContext context = JAXBContext.newInstance(c);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		t = (T) unmarshaller.unmarshal(new StringReader(xml));
		return t;
	}
}
