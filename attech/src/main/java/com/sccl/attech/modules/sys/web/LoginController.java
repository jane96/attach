/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.utils.CacheUtils;
import com.sccl.attech.common.utils.CookieUtils;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author sccl
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+"/";
		}
		return "sysLogin.jsp";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){

			return "redirect:/";
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		return "sysLogin.jsp";
	}
	
	@RequestMapping(value = "/login.ws", method = RequestMethod.GET)
	public String login1(String username,String password, HttpServletRequest request, HttpServletResponse response, Model model) {
		request.setAttribute("username", username);
		request.setAttribute("password", password);
		return "sysLoginPhone.jsp";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresUser
	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:/login";
		}
		
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		// 登录成功后，获取上次登录的当前站点ID
		UserUtils.putCache("siteId", CookieUtils.getCookie(request, "siteId"));
		CookieUtils.setCookie(response, "waiqingUser", user.getId());
		return "index.html";
	}
	
	
	
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		
		return loginFailNum >= 100;//未启用验证码
	}
	

	@SuppressWarnings("resource")
	@RequestMapping("${adminPath}/download")
	public String download(@RequestParam String filePath,HttpServletRequest request,HttpServletResponse response) {
		String fp =  filePath.replaceAll("\\&quot;", "");
		String fpe = EncodedUtil.encodeValue(fp);
		System.out.println(fpe);
		File file = new File(fpe);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(fpe);
			response.reset();
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			OutputStream outputStream = new BufferedOutputStream(
					response.getOutputStream());
			byte data[] = new byte[1024];
			while (inputStream.read(data, 0, 1024) >= 0) {
				outputStream.write(data);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("upload.htm/{referenceId}/{tableName}/{businessType}/{pathName}/{num}")
	@ResponseBody
	public String upload(MultipartFile file,@RequestParam(defaultValue="other",required=false) String group,@PathVariable("referenceId") String referenceId,@PathVariable("tableName") String tableName,@PathVariable("businessType") String businessType,@PathVariable("pathName") String pathName,@PathVariable("num") String num){
		String fileName = "";
		if (null==file||file.isEmpty()) {
			System.out.println("文件未上传");
		} else {
			/*System.out.println(referenceId);
			System.out.println(tableName);
			System.out.println(EncodedUtil.encodeValue(businessType));
			System.out.println(EncodedUtil.encodeValue(pathName));*/
			
			//String realPath = SpringContextHolder.getRootRealPath() + Global.getCkBaseDir();
			String realPath =  Global.getCkBaseDir();
			
			
			if("".equals(pathName)||pathName==null){
				fileName = group+File.separator+DateUtils.getDate("yyyyMM") + File.separator + DateUtils.getDay() + File.separator + RandomUtils.nextLong() + "."
						+FilenameUtils.getExtension(file.getOriginalFilename());
			}else{
				fileName = EncodedUtil.encodeValue(pathName)+File.separator+DateUtils.getDate("yyyyMM") + File.separator + DateUtils.getDay() + File.separator + RandomUtils.nextLong() + "."
						+FilenameUtils.getExtension(file.getOriginalFilename());
			}
			
			
			// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
			try {
				
				
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				fileName=Global.getCkBaseDir() + fileName.replace("\\", "/");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileName;
		}
		return fileName;
	}
}
