/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.web;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.HttpClictUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExportExcel;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.message.entity.AlarmRecords;
import com.sccl.attech.modules.message.service.AlarmRecordsService;
import com.sccl.attech.modules.message.vo.AlarmInboxVo;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 告警记录Controller
 * @author denghc
 * @version 2015-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/message/alarmRecords")
public class AlarmRecordsController extends BaseController {

	@Autowired
	private AlarmRecordsService alarmRecordsService;
	
	@ModelAttribute
	public AlarmRecords get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return alarmRecordsService.get(id);
		}else{
			return new AlarmRecords();
		}
	}
	
	@RequiresPermissions("message:alarmRecords:view")
	@RequestMapping(value = {"list", ""})
	public String list(AlarmRecords alarmRecords, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			alarmRecords.setCreateBy(user);
		}
		if(alarmRecords.getUser()!=null&&StringUtils.isNotEmpty(alarmRecords.getUser().getName())){
			User alarmUser = alarmRecords.getUser();
			String searchLable = EncodedUtil.decodeValue(alarmRecords.getUser().getName());
			alarmUser.setName(searchLable);
		}
        Page<AlarmRecords> page = alarmRecordsService.find(new Page<AlarmRecords>(request, response), alarmRecords); 
        model.addAttribute("page", page);
        try {
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//@RequiresPermissions("message:alarmRecords:view")
	@RequestMapping(value = "form")
	public String form(AlarmRecords alarmRecords, Model model) {
		model.addAttribute("alarmRecords", alarmRecords);
		return "modules/" + "message/alarmRecordsForm";
	}

	//@RequiresPermissions("message:alarmRecords:edit")
	@RequestMapping(value = "save.ws")
	@ResponseBody
	public Map<String, Object> save(AlarmRecords alarmRecords,String key,String license,Double xalis,Double yalis) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(AlarmRecords=" + alarmRecords + ", String=" + key + ", String=" + license + ", Double=" + xalis + ", Double=" + yalis + ") - 开始");
		}
		
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		resultMap.put("status", "-1");
		if(StringUtils.isNotBlank(validREST(key, PHONE_REST_SALT, license)))
			return resultMap;
		if(Global.isDemoMode()){
			resultMap.put("message", "演示模式，不允许操作！");

			if (logger.isDebugEnabled()) {
				logger.debug("save(AlarmRecords=" + alarmRecords + ", String=" + key + ", String=" + license + ", Double=" + xalis + ", Double=" + yalis + ") - 结束 - 返回值=" + resultMap);
			}
			return resultMap;
		}
		alarmRecords.setLocation_desc(HttpClictUtil.getInfo(yalis, xalis));
		alarmRecordsService.save(alarmRecords);
		resultMap.put("status", "1");
		resultMap.put("message", "告警成功!");

		if (logger.isDebugEnabled()) {
			logger.debug("save(AlarmRecords=" + alarmRecords + ", String=" + key + ", String=" + license + ", Double=" + xalis + ", Double=" + yalis + ") - 结束 - 返回值=" + resultMap);
		}
		return resultMap;
	}
	
	//@RequiresPermissions("message:alarmRecords:edit")
	@RequestMapping(value = "delete")
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		alarmRecordsService.delete(id);
		resultMap.put("status", "1");
		resultMap.put("message", "记录删除成功!");
		return resultMap;
	}
	/**
	 * 告警记录导出
	 * @param office
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	//@RequiresPermissions("message:alarmRecords:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Office office, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			office.setId("1");
			String fileName = "告警记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<AlarmRecords> page = alarmRecordsService.find(new Page<AlarmRecords>(request, response, -1), office);
			new ExportExcel("告警记录", AlarmRecords.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出告警记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/office/?repage";
	}
	
	
	/**
	 * 获取所有告警记录(state 为1(已读)，state 为0(未读)) 未分页
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findAllAlarm"})
	@ResponseBody
	public List<AlarmInboxVo> findAllNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        String offficeId  = user.getOffice().getId();//管理人员的部门ID
        List<AlarmRecords> nrList = alarmRecordsService.findAllAlarm(offficeId);
        List<AlarmInboxVo> nrvList = new ArrayList<AlarmInboxVo>();
        if(nrList!=null&&nrList.size()>0){
	        for (AlarmRecords nr : nrList) {
	        	AlarmInboxVo nrv = new AlarmInboxVo();
	        	
	        	nrv.setId(nr.getId());
	        	nrv.setDate(nr.getLocation_date());
	        	nrv.setContent(nr.getContent());
	        	nrv.setFrom(nr.getName());
	        	nrv.setFromId(nr.getUser().getName());
	        	nrv.setAvatar(nr.getUser().getPhoto());
	        	nrv.setState(nr.getState());
	        	nrv.setSubject(nr.getTypeName());
	        	nrvList.add(nrv);
			}
        }
		return nrvList;
	}
	
	/**
	 * 获取最新告警记录前5个(state 为1(已读)，state 为0(未读)) 
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findSomeAlarm"})
	@ResponseBody
	public List<AlarmInboxVo> findSomeNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		
		List<AlarmInboxVo> nrvList = new ArrayList<AlarmInboxVo>();
		
		if(user!=null && user.getOffice()!=null)
		{
			String offficeId  = user.getOffice().getId();//管理人员的部门ID
	        List<AlarmRecords> nrList = alarmRecordsService.findSomeAlarm(offficeId);
	        if(nrList!=null&&nrList.size()>0){
		        for (AlarmRecords nr : nrList) {
		        	AlarmInboxVo nrv = new AlarmInboxVo();
		        	
		        	nrv.setId(nr.getId());
		        	nrv.setDate(nr.getLocation_date());
		        	nrv.setContent(nr.getContent());
		        	nrv.setFrom(nr.getName());
		        	nrv.setFromId(nr.getUser().getName());
		        	nrv.setAvatar(nr.getUser().getPhoto());
		        	nrv.setState(nr.getState());
		        	nrv.setSubject(nr.getTypeName());
		        	nrvList.add(nrv);
				}
	        }
		}
		return nrvList;
	}
	
	
	/**
	 * 根据id改变告警的已读状态
	 * @param state
	 * @param noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"changeAlarmState"})
	public String changeNoticeState(@RequestParam(required=false) String state,@RequestParam(required=false) String alarmId,HttpServletRequest request, HttpServletResponse response, Model model) {
		alarmRecordsService.changeNoticeState(state,alarmId);
		return null;
	}
	
	/**
	 * 发现是否有未读告警记录
	 * @param noticeTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("message:noticeReceiver:view")
	@RequestMapping(value = {"findIsNewAlarm"})
	@ResponseBody
	public String findIsNewNotice(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if(user!=null&&user.getOffice()!=null)
		{
			String offficeId  = user.getOffice().getId();//管理人员的部门ID
	        String noticeSize = alarmRecordsService.findIsNotice(offficeId);
	        return noticeSize;
		}
		return null;
	}
	
	//导出文件
	@RequestMapping(value = "exportAlarm")
	@ResponseBody
	public String exportFile(AlarmRecords alarmRecords , HttpServletRequest request, HttpServletResponse response) {
		String n=alarmRecordsService.exportFile(alarmRecords, request, response);
		return n;
	}

}
