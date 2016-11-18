/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.AlarmRecords;
import com.sccl.attech.modules.message.entity.NoticeSample;
import com.sccl.attech.modules.message.service.NoticeSampleService;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 通知记录Controller
 * @author sccl
 * @version 2015-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/message/noticeSample")
public class NoticeSampleController extends BaseController {

	@Autowired
	private NoticeSampleService noticeSampleService;
	
	@ModelAttribute
	public NoticeSample get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return noticeSampleService.get(id);
		}else{
			return new NoticeSample();
		}
	}
	
	@RequiresPermissions("message:noticeSample:view")
	@RequestMapping(value = {"list", ""})
	public String list(NoticeSample noticeSample, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		if(StringUtils.isNotBlank(noticeSample.getContent())){
			String searchLable = EncodedUtil.decodeValue(noticeSample.getContent());
			noticeSample.setContent(searchLable);
		}
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			noticeSample.setCreateBy(user);
		}
        Page<NoticeSample> page = noticeSampleService.find(new Page<NoticeSample>(request, response), noticeSample); 
        model.addAttribute("page", page);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//@RequiresPermissions("message:noticeSample:view")
	@RequestMapping(value = "form")
	public String form(NoticeSample noticeSample, Model model) {
		model.addAttribute("noticeSample", noticeSample);
		return "modules/" + "message/noticeSampleForm";
	}

	@RequiresPermissions("message:noticeSample:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(NoticeSample noticeSample, Model model, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		noticeSample.setCompany(user.getCompany());
		noticeSample.setOffice(user.getOffice());
		noticeSample.setCreateDate(new Date());
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		noticeSampleService.save(noticeSample);
		resultMap.put("status", "1");
		resultMap.put("message", "模板编辑成功!");
		return resultMap;
	}
	
	@RequiresPermissions("message:noticeSample:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		noticeSampleService.delete(id);
		resultMap.put("status", "1");
		resultMap.put("message", "模板删除成功!");
		return resultMap;
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(NoticeSample noticeSample , HttpServletRequest request, HttpServletResponse response) {
		String n=noticeSampleService.exportFile(noticeSample, request, response);
		return n;
	}

}
