/**
 * @(#)UpdateFileController.java  2015-07-14  2015-07-14 15:59:09 中国标准时间
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.mobile.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.Collections3;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.mobile.entity.UpdateFile;
import com.sccl.attech.modules.mobile.service.UpdateFileService;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * The App升级Controller
 * 
 * @author zzz
 * @version 2015-07-14, 2015-07-14 15:59:09 中国标准时间
 * @see com.sccl.attech.modules.mobile.web
 * @since JDK1.7
 */
@Controller
@RequestMapping(value = "${adminPath}/mobile/updateFile")
public class UpdateFileController extends BaseController {

	@Autowired
	private UpdateFileService updateFileService;
	
	@ModelAttribute
	public UpdateFile get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return updateFileService.get(id);
		}else{
			return new UpdateFile();
		}
	}
	
	@RequiresPermissions("mobile:updateFile:view")
	@RequestMapping(value = {"list", ""})
	@ResponseBody
	public Page<UpdateFile> list(UpdateFile updateFile, HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(UpdateFile=" + updateFile + ", HttpServletRequest, HttpServletResponse) - 开始");
		}
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			updateFile.setCreateBy(user);
		}
        Page<UpdateFile> page = updateFileService.find(new Page<UpdateFile>(request, response), updateFile);
        if (logger.isDebugEnabled()) {
			logger.debug("list(UpdateFile=" + updateFile + ", HttpServletRequest, HttpServletResponse) - 结束 - 返回值=" + page);
		} 
		return page;
	}

	@RequiresPermissions("mobile:updateFile:view")
	@RequestMapping(value = "form")
	@ResponseBody
	public UpdateFile form(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("form(String=" + id + ") - 开始");
		}
	
		if (StringUtils.isBlank(id))
			return null;
		UpdateFile updateFile = get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("form(String=" + id + ") - 结束 - 返回值=" + updateFile);
		}
		return updateFile;
	}

	@RequiresPermissions("mobile:updateFile:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public ResultData save(UpdateFile updateFile,Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(UpdateFile=" + updateFile + ") - 开始");
		}
		ResultData result = new ResultData("保存App升级成功");
		if (!beanValidator(model, updateFile)){
			result.setSuccess(false);
			result.setMessage("校验'" + updateFile.getName() + "'失败,"+model.asMap().get("message"));
			return result;
		}
		try {
			updateFileService.save(updateFile);
		} catch (Exception e) {
			logger.error("保存App升级失败", e);
			result.setSuccess(false);
			result.setMessage("保存App升级失败," + e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("save(UpdateFile=" + updateFile + ") - 结束 - 返回值=" + result);
		}
		return result;
	}
	
	@RequiresPermissions("mobile:updateFile:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public ResultData delete(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 开始");
		}
		ResultData result = new ResultData("删除App升级成功");
		try {
			updateFileService.delete(id);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("删除App升级失败," + e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 结束 - 返回值=" + result);
		}
		return result;
	}

	@RequestMapping(value = "updateApp.ws")
	@ResponseBody
	public UpdateFile updateApp(String key,String license) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ") - 开始");
		}

		UpdateFile updateFile= new UpdateFile();
		if(StringUtils.isNotBlank(validREST(key, PHONE_REST_SALT, license)))
			return updateFile;
		
		Page<UpdateFile> page = new Page<UpdateFile>();
		page.setPageSize(1);
		
		page = updateFileService.find(page, new UpdateFile());
		
		updateFile=Collections3.getFirst(page.getList());
		
		if (logger.isDebugEnabled()) {
			logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ") - 结束 - 返回值=" + updateFile);
		}
		return updateFile;
	}
	@RequestMapping(value = "getUpdatePath")
	@ResponseBody
	public Map<String,String> getFilePath(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String, String>();
		String n=updateFileService.find();
		map.put("path", n);
		return map;
	}
}
