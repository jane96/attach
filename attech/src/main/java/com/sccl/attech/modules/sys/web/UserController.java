/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.hibernate.service.jta.platform.internal.JOnASJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.sccl.attech.common.beanvalidator.BeanValidators;
import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.mapper.JsonMapper;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.JsonToBean;
import com.sccl.attech.common.utils.JsonUtils;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.QuarUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExportExcel;
import com.sccl.attech.common.utils.excel.ImportExcel;
import com.sccl.attech.common.vo.BaseView;
import com.sccl.attech.common.vo.Point;
import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.Role;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;
import com.sccl.attech.modules.sys.service.OfficeService;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.service.UserOfficeService;
import com.sccl.attech.modules.sys.service.UserService;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.sys.vo.MobileUserVo;
import com.sccl.attech.modules.sys.vo.UserVo;

/**
 * 用户Controller
 * @author sccl
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private UserOfficeService userOfficeService;
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}
	/**
	 * 查询出登录用户
	 * @return
	 */
	@RequestMapping(value = "findLoginUser")
	@ResponseBody
	public User findLoginUser(){
		
		User user = UserUtils.getUser();
		
		return user;
	}
	/**
	 * 根据ID查询用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "findUserById")
	@ResponseBody
	public User findUserById(String id){
		
		User user = systemService.getUser(id);
		
		return user;
	}
	
	
	/**
	 * 根据ids查询用户信息
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "findShowAddress")
	@ResponseBody
	public List<UserVo> findShowAddress(String ids){
		
		List<User> list= systemService.findShowAddress(ids);
		List<UserVo> userVos = new ArrayList<UserVo>();
		if(list != null && list.size() >0){
			for(User u : list){
				UserVo vo = new UserVo(u.getId(), u.getName(), u.getEmail(), u.getMobile(), u.getPhoto(), u.getOfficeName());
				if(u.getLocationOn() != null){
					if(u.getLocationOn() ==1){
						vo.setLocationOnText("取消定位");
					}else{
						vo.setLocationOnText("激活定位 ");
					}
				}
				userVos.add(vo);
//				for(LocateRecorder o:list2){
//					System.out.println(DateUtils.formatDate(o.getLocationDate(), "yyyy-MM-dd HH:mm:ss"));
//				}
			}
		}
		
		return userVos;
	}
	/*
	 * user是哪里来的？
	 * */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"list", "","list.do"})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        return null;
	}
	
	/**
	 * 查询部门及一下所有员工
	 * @param user
	 * @param parentId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"companyList", "","companyList.do"})
	public String companyList(User user,String parentId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {		
		List<User> list= systemService.findUserByOffices(parentId, user); 
        model.addAttribute("page", list);
        this.sendObjectToJson(list, response);
		return null;
	}
	
	/**
	 *手机端验证登录 
	 * @param LoginName
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "validateLogin.ws")
	@ResponseBody
	public MobileUserVo validateLogin(String key,String license,String loginName,String pwd){
		if (logger.isDebugEnabled()) {
			logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ", String=" + loginName + " String="+pwd+") - 开始");
		}
		
		ResultData data=new ResultData("登录失败！");
		if(StringUtils.isNotBlank(validREST(key, PHONE_REST_SALT, license))){
			return null;
		}		
		User loginUser=systemService.getUserByLoginName(loginName);
		pwd=Md5Util.md5Encoder(pwd);
		MobileUserVo mobileUser=new MobileUserVo();
		if(loginUser!=null){			
		
	
	
			if(pwd.equals(loginUser.getPassword())){
				mobileUser.setCompanyName(loginUser.getCompany().getName());
				mobileUser.setCompanyId(loginUser.getCompany().getId());
				mobileUser.setUserName(loginUser.getName());
				mobileUser.setUserId(loginUser.getId());
				if(loginUser.getOutId()!=null){
					mobileUser.setOutId(loginUser.getOutId());
				}else{
					mobileUser.setOutId(" ");
				}
				mobileUser.setStatus("1");
				mobileUser.setMessage("登录成功！");
				return mobileUser;
			}else{
				mobileUser.setStatus("0");
				mobileUser.setMessage("用户名或密码错误！");
				return mobileUser;
			}						
		}else{
			mobileUser.setStatus("-1");
			mobileUser.setMessage("该用户不存在！");		
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ", String=" + loginName + ",String=" + pwd + ") - 结束 - 返回值=" + mobileUser);
		}
	
		return mobileUser;
		
	}
	
	/**
	 * 手机客户端修改密码
	 * @param loginName 登录名
	 * @param oldPwd	旧密码
	 * @param newPwd	新密码
	 * @return
	 */
	@RequestMapping(value = "mobileUpdatePwd.ws")
	@ResponseBody
	public  Map<String,Object> mobileUpdatePwd(String key,String license,String loginName,String oldPwd,String newPwd){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user=systemService.getUserByLoginName(loginName);
	
		if (logger.isDebugEnabled()) {
			logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ", String=" + loginName + " String="+oldPwd+" String="+newPwd+") - 开始");
		}
		ResultData data=new ResultData("修改失败！");
		if(StringUtils.isNotBlank(validREST(key, PHONE_REST_SALT, license))){
			return null;
		}	
		
	if (StringUtils.isNotBlank(oldPwd) && StringUtils.isNotBlank(newPwd)){
			
			if (SystemService.validatePassword(oldPwd, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPwd);
				resultMap.put("status", 1);
				resultMap.put("message", "修改密码成功");
			}else{
				resultMap.put("status", 0);
				resultMap.put("message", "修改密码失败，旧密码错误");
			}
		}
		
	if (logger.isDebugEnabled()) {
		logger.debug("getDataListByUserId(String=" + key + ", String=" + license + ", String=" + loginName + ",String=" + oldPwd + "String="+newPwd+" ) - 结束 - 返回值=" + resultMap);
	}
	return resultMap;
	}
	
	//导出文件
	@RequestMapping(value = "exportUser")
	@ResponseBody
	public String exportFile(User user , HttpServletRequest request, HttpServletResponse response) {
		String n=systemService.exportUser(user, request, response);
		return n;
	}
	/**
	 * 定位中心查询分页
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"ajaxList", "",})
	@ResponseBody
	public Map<String, Object> ajaxList(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
        Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
        List<User> users = page.getList();
        if(users != null && users.size() > 0){
        }
        resultMap.put("list", users);
        resultMap.put("count", Page.maxPageSize(page.getCount(), page.getPageSize()));
		return resultMap;
	}
	/**
	 * 定位中心查询出在框内的外勤人员
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "findPullBox")
	@ResponseBody
	public List<User> findPullBox(String ids){
		List<User> realLlist = new ArrayList<User>();
		List<Point> points = null;
		if(StringUtils.isNotBlank(ids)){
			points = QuarUtil.getPointAtt(ids);
		}
		if(points.get(0).toString().equals(points.get(1).toString())){
			return realLlist;
		}
		//把外勤人员经纬封装到集合里面
		List<User> users = systemService.findAllUserConditon(null);
		if(users != null && users.size() > 0){
        	//判断点是否在矩形里面
        	for(User u : users){
        		if(u.getXaxis() == null){
        			continue;
        		}
        		boolean flag = QuarUtil.isContain(points.get(0), points.get(1), points.get(2), points.get(3), new Point(u.getXaxis(), u.getYaxis()));
        		if(flag){
        			realLlist.add(u);
        		}
        	}
        	
        }
		return realLlist;
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping("form")
	public String form(User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}

		// 判断显示的用户是否在授权范围内
		String officeId = user.getOffice().getId();
		User currentUser = UserUtils.getUser();
		if (!currentUser.isAdmin()) {
			String dataScope = systemService.getDataScope(currentUser);
			// System.out.println(dataScope);
			if (dataScope.indexOf("office.id=") != -1) {
				String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10, dataScope.indexOf(" or"));
				if (!AuthorizedOfficeId.equalsIgnoreCase(officeId)) {
					return "error/403";
				}
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/userForm";
	}
	/**
	 * 查询所有角色返回json
	 * @author luoyang
	 * @return
	 */
	@RequestMapping("findRoleList")
	@ResponseBody
	public List<Role> findRoleList(){
		List<Role> list = null;
		List<Role> realList = new ArrayList<Role>();
		try {
			list = systemService.findAllRole();
			if(list != null && list.size() > 0){
				for(Role r : list){
					Role role = new Role(r.getId(), r.getName());
					realList.add(role);
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return realList;
	}
	/**
	 * 根据组织机构查询角色(超级管理员使用)
	 * @return
	 */
	@RequestMapping("findByOrangNameRoleList")
	@ResponseBody
	public List<Role> findByOrangNameRoleList(String companyId){
		List<Role> list = null;
		List<Role> realList = new ArrayList<Role>();
		boolean b = true;
		try {
			//TODO  修改查询方法
			list = systemService.findByOrangAllRole(companyId,null);
			if(list != null && list.size() > 0){
				for(Role r : list){
					Role role = new Role(r.getId(), r.getName());
					realList.add(role);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return realList;
	}
	@RequestMapping("findRoleList1")
	public String findRoleList1(HttpServletRequest request){
		List<Role> list = null;
		List<Role> realList = new ArrayList<Role>();
		try {
			list = systemService.findAllRole();
			if(list != null && list.size() > 0){
				for(Role r : list){
					Role role = new Role(r.getId(), r.getName());
					realList.add(role);
				}
			}
		request.setAttribute("list", list);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return "pages/user/pages_users";
	}
	
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		
		//存放返回结果的Map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
//			if (Global.isDemoMode()) 
//			{
//				addMessage(redirectAttributes, "演示模式，不允许操作！");
//				return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
//			}
			
			JSONObject jsonObject=JSONObject.fromObject(request.getParameter("getData"));
			
			List<Office> officeList= new ArrayList<Office>();	
			
			//部门JSON串
			JSONArray deptArr=null;
			
			try 
			{
				deptArr=jsonObject.getJSONArray("deptInfo");
			} 
			catch (Exception e) 
			{
				resultMap.put("status", 2);
				resultMap.put("message","请选择归属机构！");
				return resultMap;
			}
			
			
			for(Object object:deptArr)
			{
				JSONObject jsonObj=JSONObject.fromObject(object);
				
				//职位JSON
				Object bussiType=jsonObj.get("bussiType");
				
				//移除职位JSON串
				jsonObj.remove("bussiType");
				
				//将移除的JSON串转化为部门对象
				Office office=(Office)JsonToBean.getJavaDTO(jsonObj.toString(), Office.class);
				
				if(bussiType == null)
				{
					resultMap.put("status", 2);
					resultMap.put("message","用户在部门【"+office.getName()+"】未选择职位");
					
					return resultMap;
				}
				
				//职位信息
				Dict dict=(Dict)JsonToBean.getJavaDTO(bussiType.toString(),Dict.class);
				office.setDict(dict);
				
				//公司信息
				Office company=this.officeService.get(office.getCompanyId());
				office.setParent(company);
				
				officeList.add(office);
			}
			
			//移除部门JSON
			jsonObject.remove("deptInfo");
			
			
			JSONArray roleArr=jsonObject.getJSONArray("newRoleList");
			jsonObject.remove("newRoleList");
			
			//用户数据
			User user=(User)JsonToBean.getJavaDTO(jsonObject.toString(),User.class);
			
			//用户所选的角色集合
			List<Role> roleList=JsonToBean.getDTOList(roleArr.toString(), Role.class);
			
			//删除用户角色、用户部门等关系
			if(user.getId()!=null && !user.getId().equals(""))
			{
				User userc= this.userService.get(user.getId());
				
				//检查登陆账号是否存在
				if (!checkLoginName(userc.getLoginName(),user.getLoginName()))
				{
					resultMap.put("status", 2);
					resultMap.put("message","登陆名'" + user.getLoginName() + "'已存在!");
					
					return resultMap;
				}
				
			}
			else
			{
				//检查登陆账号是否存在
				User userc = this.systemService.getUserByLoginName(user.getLoginName());
				if(userc!=null)
				{
					resultMap.put("status", 2);
					resultMap.put("message", "登陆名'" + user.getLoginName() + "'已存在!");
					
					return resultMap;
				}
				
				//新增用户设置默认密码
				user.setPassword(Md5Util.md5Encoder("123456"));
			}
			
			user.setRoleList(roleList);
			this.systemService.saveUser(user,officeList);
			
			resultMap.put("status", 1);
			resultMap.put("message", "保存用户'" + user.getLoginName() + "'成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 2);
			resultMap.put("message", "保存失败，请联系管理员！");
		}
		
		return resultMap;
	}
	
	
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(String id, String userOfficeId,RedirectAttributes redirectAttributes) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (UserUtils.getUser().getId().equals(id)) {
			resultMap.put("status", "fault");
			resultMap.put("message", "删除用户失败, 不允许删除当前用户");
			//addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		} else if (User.isAdmin(id)) {
			resultMap.put("status", "fault");
			resultMap.put("message", "删除用户失败, 不允许删除超级管理员用户");
			//addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		} else {
			systemService.deleteUser(id,userOfficeId);
			resultMap.put("status", "success");
			resultMap.put("message", "删除用户成功");
			//addMessage(redirectAttributes, "删除用户成功");
		}
		return resultMap;
	}
	
	/**
	 * 重置密码
	 * @param id
	 * @param newPwd
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("reset")
	@ResponseBody
	public Map<String, Object> reset(String id,String newPwd,RedirectAttributes redirectAttributes) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		systemService.updatePasswordById(id,"",newPwd);		
			resultMap.put("status", "success");
			resultMap.put("message", "重置密码成功，新密码为‘123456’！");
		return resultMap;
	}
	
	
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx"; 
    		Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user); 
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }

	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
		}
		
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list){
				try{
					if ("true".equals(checkLoginName("", user.getLoginName()))){
						user.setPassword(SystemService.entryptPassword("123456"));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum+" 条用户" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }
	
	@RequiresPermissions("sys:user:view")
    @RequestMapping("import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			list.add(UserUtils.getUser());
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("checkLoginName")
	public Boolean checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return true;
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return true;
		}
		return false;
	}

	@RequiresUser
	@RequestMapping("info")
	public String info(User user, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			
			currentUser = UserUtils.getUser(true);
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			systemService.saveUser(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		return "modules/sys/userInfo";
	}
	
	
	
	/**
	 * 修改密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresUser
	@RequestMapping("modifyPwd")
	@ResponseBody
	public Map<String,Object> modifyPwd(String oldPassword, String newPassword, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				resultMap.put("status", 1);
				resultMap.put("message", "修改密码成功，点击确定跳转到登录页");
			}else{
				resultMap.put("status", 0);
				resultMap.put("message", "修改密码失败，旧密码错误");
			}
		}
		return resultMap;
	}
    
//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			@Autowired
//			private SystemService systemService;
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//	}
	
	/**
	 * 根据部门Id获取所在部门的用户
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"findByOffice"})
	public String findByOffice(@RequestParam(value = "searchKey", required=false) String searchKey,
								@RequestParam(value = "officeId", required=false) String officeId,
								HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        
		if(searchKey!=null){
			
			searchKey = EncodedUtil.encodeValue(searchKey);
		}
		Page<User> page = systemService.findByOffice(new Page<User>(request, response),searchKey,officeId); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}
	
	/**
	 * 根据部门Id获取所在部门的用户
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"findWorkOutByOffice"})
	public String findWorkOutByOffice(@RequestParam(value = "searchKey", required=false) String searchKey,
								@RequestParam(value = "officeId", required=false) String officeId,
								HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        
		if(searchKey!=null){
			searchKey = EncodedUtil.encodeValue(searchKey);
		}
		Page<User> page = systemService.findWorkOutByOffice(new Page<User>(request, response),searchKey,officeId); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
		return null;
	}
	
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "addWorkOut")
	@ResponseBody
	public Map<String, Object> addWorkOut(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		
		// 保存用户信息
		systemService.saveUser(user);
		if("2".equals(user.getUserType())){
			resultMap.put("status", "1");
			resultMap.put("message", "已成功添加外勤人员!");
		}else{
			resultMap.put("status", "1");
			resultMap.put("message", "已成功移除外勤人员!");	
		}
		return resultMap;
	}
	/**
	 * 省管接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param officeId2
	 * @param companyId2
	 * @param loginName
	 * @param password
	 * @param no
	 * @param name
	 * @param id
	 * @param email
	 * @param phone
	 * @param mobile
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "setUserResult")
	@ResponseBody
	public String setUserResult(String OPFlag,String TimeStamp,String hashCode,String Summary,String officeId2,String companyId2,
			String loginName,String password,String no,String name,String id,String email,String phone,String mobile,String userType){
		String resultCode = systemService.remoteUser(OPFlag, TimeStamp, hashCode, Summary, officeId2, companyId2,
				loginName, password, no, name, id, email, phone, mobile,
				userType);
		
		return resultCode;
	}

	
	/**
	 * 定位中心查询分页
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getUserDeptList")
	@ResponseBody
	public List<Map<String, Object>> getUserDeptList(String userId, HttpServletRequest request,HttpServletResponse response) throws IOException {
		try 
		{
			return this.officeService.getUserDeptList(userId);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询用户所在的所有部门
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getUserOfficeList")
	@ResponseBody
	public List<Map<String,String>>  getUserOfficeList(String type) throws IOException {
		User user = UserUtils.getUser();
		
		if(user!=null && user.getOffice()==null && user.getCompany()==null)
		{
			return this.userOfficeService.getUserDeptList(user.getId());
		}
		
		if(user!=null && type!=null && type.equals("1"))
		{
			return this.userOfficeService.getUserDeptList(user.getId());
		}
		
		return null;
	}
	
	/**
	 * 部门选择
	 * @param request
	 * @param officeId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "switchDepart")
	@ResponseBody
	public String switchDepart(HttpServletRequest request,String officeId,String companyId)
	{
		String resultCode="1";//默认失败
		
		User user = UserUtils.getUser();
		if(user!=null)
		{
			User userc=this.userService.get(user.getId());
			
			Office office=this.officeService.get(officeId);
			Office company=this.officeService.get(companyId);
			
			userc.setOffice(office);
			userc.setCompany(company);
			
			UserUtils.removeCache(UserUtils.CACHE_USER);
			UserUtils.putCache(UserUtils.CACHE_USER,userc);
			
			resultCode= "0";//切换成功
			
		}
		
		return resultCode;
	}
	
	/**
	 * 部门、职位、人员JSON
	 * @param request
	 * @param officeId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "getAllOfficeUserJson")
	@ResponseBody
	public String getAllOfficeUserJson()
	{
		return this.systemService.getAllOfficeUserJson();
	}
}
