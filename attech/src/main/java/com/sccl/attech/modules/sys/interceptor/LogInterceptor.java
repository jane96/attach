/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.SpringContextHolder;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.dao.LogDao;
import com.sccl.attech.modules.sys.entity.Log;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 系统拦截器
 * @author sccl
 * @version 2013-6-6
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

	private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null) {
			String viewName = modelAndView.getViewName();
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
			if(viewName.startsWith("modules/") && DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType())){
				modelAndView.setViewName(viewName.replaceFirst("modules", "mobile"));
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		String requestRri = request.getRequestURI();
		String uriPrefix = request.getContextPath() + Global.getAdminPath();
		
		//判断是否是调用省管接口
		Boolean flag = false;
		if(StringUtils.isNotBlank(requestRri)){
			String[] ids = requestRri.split("/");
			String zhi = ids[ids.length-1];
			if(StringUtils.isNotBlank(zhi)&&zhi.length()>4){
				String re = zhi.substring(0, 3);
				if("set".equals(re)){
					flag = true;
				}
			}
		}
		
		if ((StringUtils.startsWith(requestRri, uriPrefix) && (StringUtils.endsWith(requestRri, "/save")
				|| StringUtils.endsWith(requestRri, "/delete") || StringUtils.endsWith(requestRri, "/import")
				|| StringUtils.endsWith(requestRri, "/update")||StringUtils.endsWith(requestRri, "/login")
				|| StringUtils.endsWith(requestRri, "/logout")||StringUtils.endsWith(requestRri, "/authority")
				|| StringUtils.endsWith(requestRri, "/joinup")||flag )|| ex!=null)
				){
		
			User user = UserUtils.getUser();
			if (user!=null && user.getId()!=null){
				
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				
				String type = "";
				if(ex != null){
					type = Log.TYPE_EXCEPTION;
				}else if(StringUtils.endsWith(requestRri, "/save")){
					type=Log.TYPE_UPDATEORADD;
				}else if(StringUtils.endsWith(requestRri, "/delete")){
					type=Log.TYPE_DELETE;
				}else if(StringUtils.endsWith(requestRri, "/import")){
					type=Log.TYPE_EXPOT;
				}else if(StringUtils.endsWith(requestRri, "/update")){
					type=Log.TYPE_UPDATE;
				}else if(StringUtils.endsWith(requestRri, "/login")){
					type=Log.TYPE_LOGIN;
				}else if(StringUtils.endsWith(requestRri, "/logout")){
					type=Log.TYPE_LOGINOUT;
				}else if(StringUtils.endsWith(requestRri, "/authority")){
					type=Log.TYPE_AUTHORITY;
				}else if(flag){
					type=Log.TYPE_SGACCESS;
				}else{
					type=Log.TYPE_ACCESS;
				}
				
				Log log = new Log();
				log.setType(type);
				log.setCreateBy(user);
				log.setCompany(user.getCompany());
				log.setOffice(user.getOffice());
				log.setCreateDate(new Date());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(request.getRequestURI());
				log.setMethod(request.getMethod());
				log.setParams(params.toString());
				log.setException(ex != null ? ex.toString() : "");
				logDao.save(log);
				
				//logger.info("save log {type: {}, loginName: {}, uri: {}}, ", log.getType(), user.getLoginName(), log.getRequestUri());
				
			}else{
				
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				
				String type = "";
				if(flag){
					type=Log.TYPE_SGACCESS;
				}
				Log log = new Log();
				log.setType(type);
				log.setCreateDate(new Date());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(request.getRequestURI());
				log.setMethod(request.getMethod());
				log.setParams(params.toString());
				log.setException(ex != null ? ex.toString() : "");
				logDao.save(log);
			}
		}
		
//		logger.debug("最大内存: {}, 已分配内存: {}, 已分配内存中的剩余空间: {}, 最大可用内存: {}", 
//				Runtime.getRuntime().maxMemory(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory(), 
//				Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()); 
		
	}

}
