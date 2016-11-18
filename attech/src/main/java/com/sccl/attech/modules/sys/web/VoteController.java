package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.SystemOutLogger;
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

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.dao.CandidateDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.dao.VoteDao;
import com.sccl.attech.modules.sys.entity.Candidate;
import com.sccl.attech.modules.sys.entity.Menu;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.Vote;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/sys/vote")
public class VoteController extends BaseController {
	
	@Autowired
	private SystemService systemService;
	//最先执行的方法
	@ModelAttribute("vote")
	public Vote get(@RequestParam(required = false) String id) {
		System.out.println("最先执行的方法");
		if (StringUtils.isNotBlank(id)) {
			return systemService.getVote(id);
		} else {
			return new Vote();
		}
	}
	@RequestMapping(value="list")
	public String find(@RequestParam(value = "votingContent", required=false) String votingContent,Vote vote, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		
		System.out.println("-----------votingContent:"+ vote.getVotingContent());
		if(StringUtils.isNotBlank(vote.getVotingContent())){
			String searchLable = EncodedUtil.decodeValue(vote.getVotingContent());
			vote.setVotingContent(searchLable);
		}
		Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote,0); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        System.out.println("页面" + page.getPageNo() +" "+ page.getPageSize()+" "+page.getTotalPage());
        return null;
		
	}
	@RequestMapping(value="candidate")
	public String find(Candidate candidate,HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		
		List<Candidate> list = systemService.findAllCandidate(); 
		System.out.println("list的大小：----------------" + list.size());
		 model.addAttribute("list", list);
		this.sendObjectToJson(list, response);
        return null;
	}
	
	//为什么Menu类能进去？Vote类不能进去？
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Vote vote, Model model, RedirectAttributes redirectAttributes) {
		
		// if(Global.isDemoMode()){
		// addMessage(redirectAttributes, "演示模式，不允许操作！");
		// return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		// }
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (!beanValidator(model, vote)) {
			// return form(menu, model);
			map.put("status", 0);
			map.put("message", "数据有误！");
			return map;
		}
		List<String> list = vote.getCandidate();
		systemService.saveVote(vote,list);
		addMessage(redirectAttributes, "保存投票'" + vote.getVotingContent() + "'成功");
		map.put("status", 1);
		map.put("message", "保存投票'" + vote.getVotingContent() + "'成功");
		return map;
		
	}
	@RequestMapping(value="stop")
	@ResponseBody
	public Map stop(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		System.out.println("-----"+request.getParameter("id")+"----------");
		systemService.stopVote(request.getParameter("id"));
		Map<String, Object> map = new HashMap<String, Object>();
		addMessage(redirectAttributes, "中止操作成功！");
		map.put("status", 1);
		map.put("message","中止操作成功！");
		return map;
	}
	@RequestMapping(value="delete")
	@ResponseBody
	public Map delete(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		System.out.println("-----"+request.getParameter("id")+"----------");
		systemService.deleteVote(request.getParameter("id"));
		Map<String, Object> map = new HashMap<String, Object>();
		addMessage(redirectAttributes, "删除操作成功！");
		map.put("status", 1);
		map.put("message","删除操作成功！");
		return map;
	}
	@RequestMapping(value="select")
	@ResponseBody
	public String select(Vote vote,HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		int type = Integer.parseInt(request.getParameter("type"));
		System.out.println(request.getParameter("pageSize" + "----------------页面大小："));
		Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote, type); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        System.out.println("页面2" + page.getPageNo() +" "+ page.getPageSize()+" "+page.getTotalPage());
        return null;
		
	}
	@RequestMapping(value="myVote")
	public Map findVote(Vote vote, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		if(StringUtils.isNotBlank(vote.getVotingContent())){
			String searchLable = EncodedUtil.decodeValue(vote.getVotingContent());
			vote.setVotingContent(searchLable);
		}
		Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote,4); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        return null;
	}
	@RequestMapping(value="vote")
	@ResponseBody
	public Map vote(Vote vote,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		String voteId = request.getParameter("voteId");
		String candidateId = request.getParameter("candidateId");
		System.out.println(voteId + "------------");
		System.out.println(candidateId + "------------");
		systemService.voteVote(voteId,candidateId);
		Map<String, Object> map = new HashMap<String, Object>();
		addMessage(redirectAttributes, "投票操作成功！");
		map.put("status", 1);
		map.put("message","投票操作成功！");
		return map;
	}
	@RequestMapping(value="vote2")
	@ResponseBody
	public Map vote2(Vote vote,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		String voteId = request.getParameter("voteId");
		String candidateId = request.getParameter("candidateId");
		String[] cArray = candidateId.split(",");
		systemService.voteVote2(voteId,cArray);
		Map<String, Object> map = new HashMap<String, Object>();
		addMessage(redirectAttributes, "投票操作成功！");
		map.put("status", 1);
		map.put("message","投票操作成功！");
		return map;
	}
	@RequestMapping(value="findMyVote")
	@ResponseBody
	public Map findMyVote(Vote vote, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws IOException{
		if(StringUtils.isNotBlank(vote.getVotingContent())){
			String searchLable = EncodedUtil.decodeValue(vote.getVotingContent());
			vote.setVotingContent(searchLable);
		}
		Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote,5); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        return null;
	}
	//导出文件
		@RequestMapping(value = "export")
		@ResponseBody
		public Map exportFile(Vote vote, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
			int type = Integer.parseInt(request.getParameter("type"));
			String filePath = request.getParameter("filePath");
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			int pageCount = Integer.parseInt(request.getParameter("pageNo"));
			//Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote, type);
			Map<String, Object> map = new HashMap<String, Object>();

			try{
				systemService.exportVote(vote, type,filePath,request, response,pageSize,pageCount);
				addMessage(redirectAttributes, "导出数据成功！");
				map.put("status", 1);
				map.put("message","导出数据成功！");
			}catch(Exception e){
				addMessage(redirectAttributes, "导出数据失败！");
				map.put("status", 2);
				map.put("message","导出数据失败！");
			}finally{
				return map;
			}
		}
		//导出文件及选项
		@RequestMapping(value = "exportC")
		@ResponseBody
		public Map exportCFile(Vote vote, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
			int type = Integer.parseInt(request.getParameter("type"));
			String filePath = EncodedUtil.decodeValue(request.getParameter("filePath"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			int pageCount = Integer.parseInt(request.getParameter("pageNo"));
			//Page<Vote> page = systemService.find(new Page<Vote>(request, response), vote, type);
			Map<String, Object> map = new HashMap<String, Object>();
			
			try{
				systemService.exportCVote(vote, type,filePath,request, response,pageSize,pageCount);
				addMessage(redirectAttributes, "导出数据成功！");
				map.put("status", 1);
				map.put("message","导出数据成功！");
			}catch(Exception e){
				System.out.println("----------------------------");
				e.printStackTrace();
				System.out.println("----------------------------");
				addMessage(redirectAttributes, "导出数据失败！");
				map.put("status", 2);
				map.put("message","导出数据失败！");
			}finally{
				return map;
			}
		}
	
}
