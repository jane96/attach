/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.IOException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.message.entity.SmsIntf;
import com.sccl.attech.modules.message.service.SmsIntfService;

/**
 * 短信接口表（sms_intf）Controller
 * @author lxb
 * @version 2015-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsIntf")
public class SmsIntfController extends BaseController {

	@Autowired
	private SmsIntfService smsIntfService;
	
	@ModelAttribute
	public SmsIntf get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return smsIntfService.get(id);
		}else{
			return new SmsIntf();
		}
	}
	
	@RequiresPermissions("message:smsIntf:view")
	@RequestMapping(value = {"list", ""})
	public String list(SmsIntf smsIntf, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsIntf.setCreateBy(user);
		}
        Page<SmsIntf> page = smsIntfService.find(new Page<SmsIntf>(request, response), smsIntf); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("message:smsIntf:view")
	@RequestMapping(value = "form")
	public String form(SmsIntf smsIntf, Model model) {
		model.addAttribute("smsIntf", smsIntf);
		return "modules/" + "message/smsIntfForm";
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(SmsIntf smsIntf, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
//		if (!beanValidator(model, smsIntf)){
//			return form(smsIntf, model);
//		}
//		smsIntfService.save(smsIntf);
//		addMessage(redirectAttributes, "保存短信接口表（sms_intf）'" + smsIntf.getName() + "'成功");
		
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		smsIntfService.delete(id);
		addMessage(redirectAttributes, "删除短信接口表（sms_intf）成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}

}
