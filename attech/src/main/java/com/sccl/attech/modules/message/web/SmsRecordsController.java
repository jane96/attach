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

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.SmsRecords;
import com.sccl.attech.modules.message.service.SmsRecordsService;
import com.sccl.attech.modules.sms.response.ResultInfo;
import com.sccl.attech.modules.sms.service.SmsService;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 短信记录表（sms_records）Controller
 * @author lxb
 * @version 2015-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsRecords")
public class SmsRecordsController extends BaseController {

	@Autowired
	private SmsRecordsService smsRecordsService;

	
	@ModelAttribute
	public SmsRecords get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return smsRecordsService.get(id);
		}else{
			return new SmsRecords();
		}
	}
	@RequiresPermissions("message:smsRecords:view")
	@RequestMapping(value = {"list", ""})
	public String list(SmsRecords smsRecords, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		if(StringUtils.isNotBlank(smsRecords.getContent())){
			String searchLable = EncodedUtil.decodeValue(smsRecords.getContent());
			smsRecords.setContent(searchLable);
		}
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			smsRecords.setCreateBy(user);
		}
		String basedataType = request.getParameter("basedataType");
		if(StringUtils.isNotBlank(basedataType)){
			smsRecords.setBasedataType(basedataType);
		}
        Page<SmsRecords> page = smsRecordsService.find(new Page<SmsRecords>(request, response), smsRecords); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("message:smsRecords:view")
	@RequestMapping(value = "form")
	public String form(SmsRecords smsRecords, Model model) {
		model.addAttribute("smsRecords", smsRecords);
		return "modules/" + "message/smsRecordsForm";
	}
	@RequiresPermissions("message:smsRecords:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(SmsRecords smsRecords, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result;
		try {
			result = smsRecordsService.save(smsRecords);
			if("0".equals(result)){
				map.put("status", 1);
				map.put("message", "短信发送成功");
			}
			if("1".equals(result)){
				map.put("status", 2);
				map.put("message", "短信发送失败");
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		addMessage(redirectAttributes, "保存短信记录表（sms_records）成功");
		return map;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		smsRecordsService.delete(id);
		addMessage(redirectAttributes, "删除短信记录表（sms_records）成功");
		map.put("status", 1);
		map.put("message", "保存短信模板成功");
		return map;
	}
	
	/**
	 * 导出数据
	 * @param 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportLation(SmsRecords smsRecords, HttpServletRequest request, HttpServletResponse response) {
		String n=smsRecordsService.exportFile(smsRecords, request, response);
		return n;
	}
}
