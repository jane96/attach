/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Log;
import com.sccl.attech.modules.sys.service.LogService;
import com.sccl.attech.modules.sys.utils.DictUtils;

/**
 * 日志Controller
 * @author sccl
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.find(new Page<Log>(request, response), paramMap); 
        model.addAttribute("page", page);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // model.addAllAttributes(paramMap);
		return null;
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(@RequestParam Map<String, Object> paramMap , HttpServletRequest request, HttpServletResponse response) {
		String  n=logService.exportFile(paramMap, request, response);
		return n;
	}


	/**
	 * 获取type的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = "getLogType")
	public String getAreaType(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 List<Dict> logList = DictUtils.getDictList("sys_log_type");
		 this.sendObjectToJson(logList, response);
	     return null;
	}
}
