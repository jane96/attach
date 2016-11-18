/**
 * @(#)${ClassName}Controller.java  ${classVersion}  ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ${packageName}.${moduleName}.web${subModuleName};

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

import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.service${subModuleName}.${ClassName}Service;

/**
 * The ${functionName}Controller
 * 
 * @author ${classAuthor}
 * @version ${classVersion}, ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * @see ${packageName}.${moduleName}.web${subModuleName}
 * @since JDK1.7
 */
@Controller
@RequestMapping(value = "${r"${adminPath}"}/${urlPrefix}")
public class ${ClassName}Controller extends BaseController {

	@Autowired
	private ${ClassName}Service ${className}Service;
	
	@ModelAttribute
	public ${ClassName} get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return ${className}Service.get(id);
		}else{
			return new ${ClassName}();
		}
	}
	
	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = {"list", ""})
	@ResponseBody
	public Page<${ClassName}> list(${ClassName} ${className}, HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(${ClassName}=" + ${className} + ", HttpServletRequest, HttpServletResponse) - 开始");
		}
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			${className}.setCreateBy(user);
		}
        Page<${ClassName}> page = ${className}Service.find(new Page<${ClassName}>(request, response), ${className});
        if (logger.isDebugEnabled()) {
			logger.debug("list(${ClassName}=" + ${className} + ", HttpServletRequest, HttpServletResponse) - 结束 - 返回值=" + page);
		} 
		return page;
	}

	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = "form")
	@ResponseBody
	public ${ClassName} form(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("form(String=" + id + ") - 开始");
		}
	
		if (StringUtils.isBlank(id))
			return null;
		${ClassName} ${className} = get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("form(String=" + id + ") - 结束 - 返回值=" + ${className});
		}
		return ${className};
	}

	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public ResultData save(${ClassName} ${className},Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(${ClassName}=" + ${className} + ") - 开始");
		}
		ResultData result = new ResultData("保存${functionName}成功");
		if (!beanValidator(model, ${className})){
			result.setSuccess(false);
			result.setMessage("校验'" + ${className}.getName() + "'失败,"+model.asMap().get("message"));
			return result;
		}
		try {
			${className}Service.save(${className});
		} catch (Exception e) {
			logger.error("保存${functionName}失败", e);
			result.setSuccess(false);
			result.setMessage("保存${functionName}失败," + e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("save(${ClassName}=" + ${className} + ") - 结束 - 返回值=" + result);
		}
		return result;
	}
	
	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public ResultData delete(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 开始");
		}
		ResultData result = new ResultData("删除${functionName}成功");
		try {
			${className}Service.delete(id);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("删除${functionName}失败," + e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 结束 - 返回值=" + result);
		}
		return result;
	}

}
