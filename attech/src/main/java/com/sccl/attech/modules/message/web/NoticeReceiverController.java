/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

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
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.NoticeReceiver;
import com.sccl.attech.modules.message.service.NoticeReceiverService;
import com.sccl.attech.modules.message.vo.NoticeInboxVo;
import com.sccl.attech.modules.message.vo.NoticeReceiverVo;
import com.sccl.attech.modules.message.vo.NoticeRecordsVo;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 通知收信人员Controller
 * @author denghc
 * @version 2015-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/message/noticeReceiver")
public class NoticeReceiverController extends BaseController {

	@Autowired
	private NoticeReceiverService noticeReceiverService;
	
	@ModelAttribute
	public NoticeReceiver get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return noticeReceiverService.get(id);
		}else{
			return new NoticeReceiver();
		}
	}
	
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"list", ""})
	public String list(NoticeReceiver noticeReceiver,@RequestParam(required=false) String recordId, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			noticeReceiver.setCreateBy(user);
		}
        Page<NoticeReceiver> page = noticeReceiverService.find(new Page<NoticeReceiver>(request, response), noticeReceiver); 
        model.addAttribute("page", page);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = "form")
	public String form(NoticeReceiver noticeReceiver, Model model) {
		model.addAttribute("noticeReceiver", noticeReceiver);
		return "modules/" + "message/noticeReceiverForm";
	}

	//@RequiresPermissions("message:noticeReceiver:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(NoticeReceiver noticeReceiver, Model model, RedirectAttributes redirectAttributes) {
		
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		noticeReceiverService.save(noticeReceiver);
		resultMap.put("status", "1");
		resultMap.put("message", "记录编辑成功!");
		return resultMap;
	}
	
	//@RequiresPermissions("message:noticeReceiver:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		noticeReceiverService.delete(id);
		resultMap.put("status", "1");
		resultMap.put("message", "记录删除成功!");
		return resultMap;
	}
	
	/**
	 * 根据记录获取对应的收信人信息
	 * @param noticeReceiver
	 * @param recordId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findByRecordId"})
	public String findByRecordId(NoticeReceiver noticeReceiver,@RequestParam(required=false) String recordId, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			noticeReceiver.setCreateBy(user);
		}
        Page page = noticeReceiverService.findByRecordId(new Page<NoticeReceiver>(request, response), noticeReceiver,recordId); 
        model.addAttribute("page", page);
        List<NoticeReceiver> nrList = page.getList();
        List<NoticeReceiverVo> nrvList = new ArrayList<NoticeReceiverVo>();
        
        for (NoticeReceiver nr : nrList) {
        	NoticeReceiverVo nrv = new NoticeReceiverVo();
        	nrv.setId(nr.getId());
        	nrv.setReceiverName(nr.getReceiver().getName());
        	nrv.setAcceptingState("已接收");
        	nrv.setState(nr.getStateName());
        	nrvList.add(nrv);
		}
        page.setList(nrvList);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取通知列表(state 为1(已读)，state 为0(未读)) 分页
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findUnreadNoticeListPage"})
	public String findUnreadNoticeListPage(@RequestParam(required=false) String state, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        Page page = noticeReceiverService.findUnreadNotice(new Page<NoticeReceiver>(request, response),userId,state);
        List<NoticeReceiver> nrList = page.getList();
        List<NoticeInboxVo> nrvList = new ArrayList<NoticeInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (NoticeReceiver nr : nrList) {
	        	NoticeInboxVo nrv = new NoticeInboxVo();
	        	
	        	nrv.setId(nr.getNoticeRecords().getId());
	        	nrv.setDate(nr.getNoticeRecords().getCreateDate());
	        	nrv.setSubject(nr.getNoticeRecords().getTitle());
	        	nrv.setContent(nr.getNoticeRecords().getContent());
	        	nrv.setFrom(nr.getNoticeRecords().getSender().getName());
	        	nrv.setFromId(nr.getNoticeRecords().getSender().getId());
	        	nrv.setAvatar(nr.getNoticeRecords().getSender().getPhoto());
	        	nrv.setState(nr.getStateName());
	        	nrvList.add(nrv);
			}
        }
        page.setList(nrvList);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取非在线通知列表(state 为1(已读)，state 为0(未读)) 未分页
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findUnreadNoticeList"})
	@ResponseBody
	public List<NoticeInboxVo> findUnreadNoticeList(@RequestParam(required=false) String state,HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        List<NoticeReceiver> nrList = noticeReceiverService.findNotice(userId,state);
        List<NoticeInboxVo> nrvList = new ArrayList<NoticeInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (NoticeReceiver nr : nrList) {
	        	NoticeInboxVo nrv = new NoticeInboxVo();
	        	
	        	nrv.setId(nr.getNoticeRecords().getId());
	        	nrv.setDate(nr.getNoticeRecords().getCreateDate());
	        	nrv.setSubject(nr.getNoticeRecords().getTitle());
	        	nrv.setContent(nr.getNoticeRecords().getContent());
	        	nrv.setFrom(nr.getNoticeRecords().getSender().getName());
	        	nrv.setFromId(nr.getNoticeRecords().getSender().getId());
	        	nrv.setAvatar(nr.getNoticeRecords().getSender().getPhoto());
	        	nrv.setState(nr.getState());
	        	nrvList.add(nrv);
			}
        }
		return nrvList;
	}
	
	
	/**
	 * 发现是否有新通知
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findIsNewNotice"})
	@ResponseBody
	public String findIsNewNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        Date loginTime = user.getLoginDate();
        String noticeSize = noticeReceiverService.findIsNotice(userId,loginTime);
		return noticeSize;
	}
	
	/**
	 * 获取在线通知(state 为1(已读)，state 为0(未读)) 未分页
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findNewNoticeList"})
	@ResponseBody
	public List<NoticeInboxVo> findNewNoticeList(@RequestParam(required=false) String state,HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        Date loginTime = user.getLoginDate();
        List<NoticeReceiver> nrList = noticeReceiverService.findNewNotice(userId,loginTime,state);
        List<NoticeInboxVo> nrvList = new ArrayList<NoticeInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (NoticeReceiver nr : nrList) {
	        	NoticeInboxVo nrv = new NoticeInboxVo();
	        	
	        	nrv.setId(nr.getNoticeRecords().getId());
	        	nrv.setDate(nr.getNoticeRecords().getCreateDate());
	        	nrv.setSubject(nr.getNoticeRecords().getTitle());
	        	nrv.setContent(nr.getNoticeRecords().getContent());
	        	nrv.setFrom(nr.getNoticeRecords().getSender().getName());
	        	nrv.setFromId(nr.getNoticeRecords().getSender().getId());
	        	nrv.setAvatar(nr.getNoticeRecords().getSender().getPhoto());
	        	nrv.setState(nr.getState());
	        	nrvList.add(nrv);
			}
        }
		return nrvList;
	}
	/**
	 * 获取对应人员的所有通知(state 为1(已读)，state 为0(未读)) 未分页
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findAllNotice"})
	@ResponseBody
	public List<NoticeInboxVo> findAllNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        List<NoticeReceiver> nrList = noticeReceiverService.findAllNotice(userId);
        List<NoticeInboxVo> nrvList = new ArrayList<NoticeInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (NoticeReceiver nr : nrList) {
	        	NoticeInboxVo nrv = new NoticeInboxVo();
	        	
	        	nrv.setId(nr.getNoticeRecords().getId());
	        	nrv.setDate(nr.getNoticeRecords().getCreateDate());
	        	nrv.setSubject(nr.getNoticeRecords().getTitle());
	        	nrv.setContent(nr.getNoticeRecords().getContent());
	        	nrv.setFrom(nr.getNoticeRecords().getSender().getName());
	        	nrv.setFromId(nr.getNoticeRecords().getSender().getId());
	        	nrv.setAvatar(nr.getNoticeRecords().getSender().getPhoto());
	        	nrv.setState(nr.getState());
	        	nrv.setReceiverId(nr.getId());
	        	Long createTime = nr.getCreateDate().getTime();
	        	nrv.setState(nr.getState());
	        	nrvList.add(nrv);
			}
        }
		return nrvList;
	}
	
	/**
	 * 获取对应人员最新的前五条通知(state 为1(已读)，state 为0(未读))
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findSomeNotice"})
	@ResponseBody
	public List<NoticeInboxVo> findSomeNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String userId = user.getId();
        List<NoticeReceiver> nrList = noticeReceiverService.findSomeNotice(userId);
        List<NoticeInboxVo> nrvList = new ArrayList<NoticeInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (NoticeReceiver nr : nrList) {
	        	NoticeInboxVo nrv = new NoticeInboxVo();
	        	
	        	nrv.setId(nr.getNoticeRecords().getId());
	        	nrv.setDate(nr.getNoticeRecords().getCreateDate());
	        	nrv.setSubject(nr.getNoticeRecords().getTitle());
	        	nrv.setContent(nr.getNoticeRecords().getContent());
	        	nrv.setFrom(nr.getNoticeRecords().getSender().getName());
	        	nrv.setFromId(nr.getNoticeRecords().getSender().getId());
	        	nrv.setAvatar(nr.getNoticeRecords().getSender().getPhoto());
	        	nrv.setState(nr.getState());
	        	nrv.setReceiverId(nr.getId());
	        	Long createTime = nr.getCreateDate().getTime();
	        	nrv.setState(nr.getState());
	        	nrvList.add(nrv);
			}
        }
		return nrvList;
	}
	
	
	/**
	 * 根据id改变通知的已读状态
	 * @param state
	 * @param noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"changeNoticeState"})
	public String changeNoticeState(@RequestParam(required=false) String state,@RequestParam(required=false) String receiverId,HttpServletRequest request, HttpServletResponse response, Model model) {
		noticeReceiverService.changeNoticeState(state,receiverId);
		return null;
	}
	
}
