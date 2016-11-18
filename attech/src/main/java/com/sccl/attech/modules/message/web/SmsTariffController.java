/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.message.entity.SmsTariff;
import com.sccl.attech.modules.message.service.SmsTariffService;

/**
 * 短信收费表（sms_tariff）Controller
 * @author lxb
 * @version 2015-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsTariff")
public class SmsTariffController extends BaseController {

	@Autowired
	private SmsTariffService smsTariffService;
	
	@ModelAttribute
	public SmsTariff get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return smsTariffService.get(id);
		}else{
			return new SmsTariff();
		}
	}
	
	@RequiresPermissions("message:smsTariff:view")
	@RequestMapping(value = {"list", ""})
	public String list(SmsTariff smsTariff, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsTariff.setCreateBy(user);
		}
        Page<SmsTariff> page = smsTariffService.find(new Page<SmsTariff>(request, response), smsTariff); 
        model.addAttribute("page", page);
		return "modules/" + "message/smsTariffList";
	}

	@RequiresPermissions("message:smsTariff:view")
	@RequestMapping(value = "form")
	public String form(SmsTariff smsTariff, Model model) {
		model.addAttribute("smsTariff", smsTariff);
		return "modules/" + "message/smsTariffForm";
	}

	@RequiresPermissions("message:smsTariff:edit")
	@RequestMapping(value = "save")
	public String save(SmsTariff smsTariff, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, smsTariff)){
			return form(smsTariff, model);
		}
		smsTariffService.save(smsTariff);
//		addMessage(redirectAttributes, "保存短信收费表（sms_tariff）'" + smsTariff.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/message/smsTariff/?repage";
	}
	
	@RequiresPermissions("message:smsTariff:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		smsTariffService.delete(id);
		addMessage(redirectAttributes, "删除短信收费表（sms_tariff）成功");
		return "redirect:"+Global.getAdminPath()+"/message/smsTariff/?repage";
	}

}
