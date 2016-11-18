/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sccl.attech.common.beanvalidator.BeanValidators;
import com.sccl.attech.common.mapper.JsonMapper;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.StringUtils;

/**
 * 控制器支持类
 * 
 * @author sccl
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected static String PHONE_REST_SALT = "p112er4323e";// assistant

	public static final String PAGE_CHARSET = "text/html; charset=UTF-8";// 页面编码
	public static final String APP_KEY = "web";// appKey
	public static final String SUCCESS = "success";// 成功
	public static final String ERROR = "error";// 失败

	public static final String RETURN_STATUS = "status";// 返回json状态的标识
	public static final String RETURN_MESSAGE = "message";// 返回json状态的标识

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object,
			Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(
					ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes,
			Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(
					ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 添加Model消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,
			String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils
						.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 页面通过response来跳转
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected void sendObjectToJson(Object object, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String message = JsonMapper.getInstance().toJson(object);
		pw.write(message);
		pw.flush();
		pw.close();
	}

	protected String validREST(String key, String salt, String license) {
		if (!StringUtils.equals(Md5Util.md5Encoder(key + salt), license))
			return "0";
		return null;
	}

	// 输出JSON成功消息，返回null
	public String actionSuccess(String message) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(RETURN_STATUS, SUCCESS);
		jsonObject.put(RETURN_MESSAGE, message);
		return jsonObject.toString();
	}

	// 输出JSON错误消息，返回null
	public String actionError(String message) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(RETURN_STATUS, ERROR);
		jsonObject.put(RETURN_MESSAGE, message);
		return jsonObject.toString();
	}
	
	
	//获取通过request过来的array
	public String getJsonString(String jsonString,String key) throws IOException{
		String retJson = "";
		HashMap<String, LinkedHashMap<String, Object>>  jsonBean = null;
		if(StringUtils.isNotBlank(jsonString) && !("false").equals(jsonString)){
			jsonBean = JsonMapper.getInstance().readValue(jsonString, HashMap.class);
		}
		try {
			retJson = String.valueOf(jsonBean.get(key));
		} catch (Exception e) {
		}
		return retJson;
	}

}
