/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.NoticeSample;
import com.sccl.attech.modules.message.entity.SmsSample;
import com.sccl.attech.modules.message.service.SmsSampleService;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 短信样例表（sms_sample）Controller
 * @author lxb
 * @version 2015-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsSample")
public class SmsSampleController extends BaseController {

	@Autowired
	private SmsSampleService smsSampleService;
	
	@ModelAttribute
	public SmsSample get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return smsSampleService.get(id);
		}else{
			return new SmsSample();
		}
	}
	@RequiresPermissions("message:smsSample:view")
	@RequestMapping(value = {"list", ""})
	public String list(SmsSample smsSample, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		if(StringUtils.isNotBlank(smsSample.getContent())){
			String searchLable = EncodedUtil.decodeValue(smsSample.getContent());
			smsSample.setContent(searchLable);
		}
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsSample.setCreateBy(user);
		}
        Page<SmsSample> page = smsSampleService.find(new Page<SmsSample>(request, response), smsSample); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("message:smsSample:view")
	@RequestMapping(value = "form")
	public String form(SmsSample smsSample, Model model) {
		model.addAttribute("smsSample", smsSample);
		return "modules/" + "message/smsSampleForm";
	}
	
	@RequiresPermissions("message:smsSample:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(SmsSample smsSample, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
//		if (!beanValidator(model, smsSample)){
//			return form(smsSample, model);
//		}
		smsSampleService.save(smsSample);
//		addMessage(redirectAttributes, "保存短信样例表（sms_sample）'" + smsSample.getName() + "'成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	@RequiresPermissions("message:smsSample:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		smsSampleService.delete(id);
		addMessage(redirectAttributes, "删除短信样例表（sms_sample）成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(SmsSample smsSample , HttpServletRequest request, HttpServletResponse response) {
		String n=smsSampleService.exportFile(smsSample, request, response);
		return n;
	}

}
