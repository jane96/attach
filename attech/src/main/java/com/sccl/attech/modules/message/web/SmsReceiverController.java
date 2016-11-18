/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.IOException;
import java.net.URLDecoder;
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
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.message.entity.SmsRecords;
import com.sccl.attech.modules.message.service.SmsReceiverService;
import com.sccl.attech.modules.message.vo.SmsReceiverVo;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 短信收信人员表（sms_receiver）Controller
 * @author lxb
 * @version 2015-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsReceiver")
public class SmsReceiverController extends BaseController {

	@Autowired
	private SmsReceiverService smsReceiverService;
	
	@ModelAttribute
	public SmsReceiver get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return smsReceiverService.get(id);
		}else{
			return new SmsReceiver();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(SmsReceiver smsReceiver, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsReceiver.setCreateBy(user);
		}
		String recordsId = request.getParameter("recordsId");
		smsReceiver.setSmsRecords(new SmsRecords(recordsId));
		String condition = request.getParameter("condition");
		if(StringUtils.isNotBlank(condition)){
			condition = URLDecoder.decode(condition,"UTF-8");//get 方式提交转码
			smsReceiver.setReceiverName(condition);
			smsReceiver.setReceiverPhone(condition);
		}
		
        Page<SmsReceiver> page = smsReceiverService.find(new Page<SmsReceiver>(request, response), smsReceiver); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("message:smsReceiver:view")
	@RequestMapping(value = "form")
	public String form(SmsReceiver smsReceiver, Model model) {
		model.addAttribute("smsReceiver", smsReceiver);
		return "modules/" + "message/smsReceiverForm";
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(SmsReceiver smsReceiver, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
//		if (!beanValidator(model, smsReceiver)){
//			return form(smsReceiver, model);
//		}
		smsReceiverService.save(smsReceiver);
//		addMessage(redirectAttributes, "保存短信收信人员表（sms_receiver）'" + smsReceiver.getName() + "'成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		smsReceiverService.delete(id);
		addMessage(redirectAttributes, "删除短信收信人员表（sms_receiver）成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	@RequestMapping(value = {"getListData", ""})
	@ResponseBody
	public String getListData(SmsReceiver smsReceiver, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsReceiver.setCreateBy(user);
		}
		String recordsId = request.getParameter("recordsId");
		smsReceiver.setSmsRecords(new SmsRecords(recordsId));
		String condition = request.getParameter("condition");
		if(StringUtils.isNotBlank(condition)){
			condition = URLDecoder.decode(condition,"UTF-8");//get 方式提交转码
			smsReceiver.setReceiverName(condition);
			smsReceiver.setReceiverPhone(condition);
		}
		
        Page<SmsReceiver> page = smsReceiverService.find(new Page<SmsReceiver>(request, response), smsReceiver); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}
	
	@RequestMapping(value = {"getDialogData", ""})
	@ResponseBody
	public String getDialogData(SmsReceiver smsReceiver, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsReceiver.setCreateBy(user);
		}
		String sendId = request.getParameter("sendId");
		String receiverId = request.getParameter("receiverId");
		
		SmsReceiverVo vo = new SmsReceiverVo();
		vo.setSendId(sendId);
		vo.setReceiverId(receiverId);
		
        List<SmsReceiverVo> list = smsReceiverService.getDialogData(vo);
        this.sendObjectToJson(list, response);
		return null;
	}

}
