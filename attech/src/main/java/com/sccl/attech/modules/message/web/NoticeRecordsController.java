/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.NoticeReceiver;
import com.sccl.attech.modules.message.entity.NoticeRecords;
import com.sccl.attech.modules.message.entity.SmsSample;
import com.sccl.attech.modules.message.service.NoticeReceiverService;
import com.sccl.attech.modules.message.service.NoticeRecordsService;
import com.sccl.attech.modules.message.vo.NoticeRecordsVo;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 通知记录Controller
 * @author sccl
 * @version 2015-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/message/noticeRecords")
public class NoticeRecordsController extends BaseController {

	@Autowired
	private NoticeRecordsService noticeRecordsService;
	@Autowired
	private NoticeReceiverService noticeReceiverService;
	
	@ModelAttribute
	public NoticeRecords get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return noticeRecordsService.get(id);
		}else{
			return new NoticeRecords();
		}
	}
	
	@RequiresPermissions("message:noticeRecords:view")
	@RequestMapping(value = {"list", ""})
	public String list(NoticeRecords noticeRecords, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		if(StringUtils.isNotBlank(noticeRecords.getTitle())){
			String searchLable = EncodedUtil.decodeValue(noticeRecords.getTitle());
			noticeRecords.setTitle(searchLable);
		}
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			noticeRecords.setCreateBy(user);
		}
        Page page = noticeRecordsService.find(new Page(request, response), noticeRecords); 
        
        
        List<NoticeRecords> nrdList = page.getList();
        List<NoticeRecordsVo> nrvList = new ArrayList<NoticeRecordsVo>();
        
        for (NoticeRecords n : nrdList) {
        	NoticeRecordsVo nrv = new NoticeRecordsVo();
        	nrv.setId(n.getId());
        	nrv.setSendTime(n.getCreateDate());
        	nrv.setTitle(n.getTitle());
        	nrv.setContent(n.getContent());
        	nrv.setSenderName(n.getSender().getName());
        	nrv.setSenderId(n.getSender().getId());// receiverName,receiverId,state,acceptingState
        	//nrv.setReceiverName(receiverName);
        	List<NoticeReceiver> nrList = noticeReceiverService.findByRecordId(n.getId());
        	String reName = "";
        	String reId = "";
        	for (NoticeReceiver nr : nrList) {
        		if(nr!=null&&nr.getReceiver()!=null){
        			reName =reName+nr.getReceiver().getName()+",";
        			reId = nr.getReceiver().getId();
        		}
			}
            nrv.setReceiverName(reName);
            nrv.setReceiverId(reId);
            nrvList.add(nrv);
		}
        
        page.setList(nrvList);
        
        
        model.addAttribute("page", page);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//@RequiresPermissions("message:noticeRecords:view")
	@RequestMapping(value = "form")
	public String form(NoticeRecords noticeRecords, Model model) {
		model.addAttribute("noticeRecords", noticeRecords);
		return "modules/" + "message/noticeRecordsForm";
	}

	@RequiresPermissions("message:noticeRecords:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(NoticeRecords noticeRecords, Model model,@RequestParam(required=true) String reserverId, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		noticeRecordsService.save(noticeRecords,reserverId);
		resultMap.put("status", "1");
		resultMap.put("message", "通知发送成功!");
		return resultMap;
	}
	
	//@RequiresPermissions("message:noticeRecords:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		noticeRecordsService.delete(id);
		resultMap.put("status", "1");
		resultMap.put("message", "记录删除成功!");
		return resultMap;
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(NoticeRecords noticeRecords , HttpServletRequest request, HttpServletResponse response) {
		String n=noticeRecordsService.exportFile(noticeRecords, request, response);
		return n;
	}
}
