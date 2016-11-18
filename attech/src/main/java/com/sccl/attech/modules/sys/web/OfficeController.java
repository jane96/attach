/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.Md5Util;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExportExcel;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.common.vo.ZtreeVo;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.AreaService;
import com.sccl.attech.modules.sys.service.OfficeService;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * 
 * @author sccl
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService	officeService;
	@Autowired
	private SystemService	systemService;
	
	@Autowired
	private AreaService areaService;

	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping({ "list", "" })
	public String list(Office office, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		// User user = UserUtils.getUser();
		// if(user.isAdmin()){
		System.out.println("进入菜单测试页面");
		office.setId("1");
		// }else{
		// office.setId(user.getOffice().getId());
		// }
		/*
		 * model.addAttribute("office", office); List<Office> list = Lists.newArrayList(); List<Office> sourcelist = officeService.findAll(); Office.sortList(list, sourcelist,
		 * office.getId()); model.addAttribute("list", list);
		 * 
		 * Page<Office> page = new Page<Office>(request, response); page.setList(list); this.sendObjectToJson(page, response);
		 */

		Page<Office> page = officeService.find(new Page<Office>(request, response), office);
		List<Office> list = page.getList();
		for (Office office2 : list) {
			String roleIds = office.getRoleIds();
			String[] split = roleIds.split(",");
			String roleNames = "";
			for (String str : split) {
				if(StringUtils.isNotBlank(str)){
					User user = systemService.getUser(str);
					String name = user.getName();
					roleNames = roleNames+name+",";
				}
			}
			office2.setRoleNames(roleNames);
		}
		page.setList(list);
		model.addAttribute("page", page);
		this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public String exportFile(Office office, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			office.setId("1");
			String fileName = "部门数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Office> page = officeService.find(new Page<Office>(request, response, -1), office);
			new ExportExcel("部门数据", Office.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出部门失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/office/?repage";
	}

	@RequestMapping("treeView")
	public String getTreeData(Office office, String parentId, String type, HttpServletResponse response) throws IOException 
	{
		//默认查询当前用户公司的树
		User user1 = UserUtils.getUser();
		
		String companyId = null;
		if(user1.getCompany()!=null)
		{
			companyId = user1.getCompany().getId();
		}
		
		List<Office> list = officeService.getTreeData(parentId,companyId,type);
		this.sendObjectToJson(list, response);
		return null;
	}
	
	@RequestMapping("getOfficeData")
	public String getOfficeData(String officeId, HttpServletResponse response) throws IOException 
	{
		Office office = this.officeService.get(officeId);
		this.sendObjectToJson(office, response);
		
		return null;
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping("form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent() == null || office.getParent().getId() == null) {
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea() == null) {
			office.setArea(office.getParent().getArea());
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(Office office, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (office.getId() == null && office.getParentId() == null) 
		{
			office.setParent(new Office(1 + ""));
			office.setArea(new Area(2 + ""));
		} 
		else 
		{
			office.setParent(new Office(office.getParentId()));
			office.setArea(new Area(office.getAreaId()));
		}

		if (!beanValidator(model, office)) {
			map.put("status", 0);
			return map;
		}
		officeService.save(office);
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		map.put("status", 1);
		map.put("message", "保存机构'" + office.getName() + "'成功");
		// return "redirect:" + Global.getAdminPath() + "/sys/office/";
		return map;
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		// if (Global.isDemoMode()) {
		// addMessage(redirectAttributes, "演示模式，不允许操作！");
		// return "redirect:" + Global.getAdminPath() + "/sys/office/";
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		if (Office.isRoot(id)) {
			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");

			map.put("status", 0);
			map.put("message", "删除机构失败, 不允许删除顶级机构或编号空");
		} else {
			officeService.delete(id);
			addMessage(redirectAttributes, "删除机构成功");

			map.put("status", 1);
			map.put("message", "删除机构成功");
		}
		return map;
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping("treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response, @RequestParam(required = false) String extId, @RequestParam(required = false) Long type,
			@RequestParam(required = false) Long grade) {

		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();

		// User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);

			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))) {

				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 根据公司选人模态框
	 * @param office
	 * @param parentId
	 * @param type
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getChildren")
	public String getChildren(Office office, String parentId, String type, HttpServletResponse response) throws IOException {
		String companyId=null;
		if(StringUtils.isEmpty(parentId))
		{
			User user = UserUtils.getUser();//默认查询当前用户公司的树
			if(user.isAdmin())
			{
				companyId = "";
			}
			else if(user.getCompany()!=null)
			{
				companyId = user.getCompany().getId();
			}
		}
		Map<String, Object> list = officeService.getChildren(companyId,(parentId == null || "".equals(parentId)) ? "" : parentId, type);
		this.sendObjectToJson(list, response);
		return null;
	}

//	/**
//	 * 部门树
//	 * 
//	 * @param extId 当前区域id
//	 * @param id 根节点区域id
//	 * @param response
//	 * @return
//	 */
//	@RequiresUser
//	@ResponseBody
//	@RequestMapping(value = "officeTreeData")
//	public List<AllTreeVo> treeData(@RequestParam(required = false) String extId, @RequestParam(required = true) String id, HttpServletResponse response) {
//		response.setContentType("application/json; charset=UTF-8");
//		AllTreeVo allTreeVo = new AllTreeVo();
//		allTreeVo = officeService.getOfficeTree(null, officeService.get(id), extId);
//		List<AllTreeVo> areaTreeList = Lists.newArrayList();
//		areaTreeList.add(allTreeVo);
//		return areaTreeList;
//	}
	
	/**
	 * ajax部门树
	 * 
	 * @param extId 当前区域id
	 * @param id 根节点区域id
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "officeTreeData")
	public String treeData(String parentId,String type, HttpServletResponse response) {
		String companyId =null;
		if(StringUtils.isEmpty(parentId)){
			User user = UserUtils.getUser();//默认查询当前用户公司的树
//			if(user.isAdmin())
//			{
//				companyId=null;
//			}
//			else if(user.getCompany()!=null)
//			{
//				companyId = user.getCompany().getId();
//			}
			
			if(user.getCompany()!=null)
			{
				companyId = user.getCompany().getId();
			}
		}
		List<Office> list = officeService.getOfficeTree(parentId,companyId,type);
		try {
			this.sendObjectToJson(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ajax公司树
	 * 
	 * @param extId 当前区域id
	 * @param id 根节点区域id
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "companyTreeData")
	public String treeDataByComp(String parentId,String type, HttpServletResponse response) {
		String companyId ="";
		if(StringUtils.isEmpty(parentId)){
			User user = UserUtils.getUser();//默认查询当前用户公司的树
			if(user.getCompany()!=null ){
				companyId = user.getCompany().getId();
				//parentId = user.getCompany().getParent().getId();
			}
		}
		List<Office> list = officeService.getCompanyTree((parentId == null || "".equals(parentId)) ? "" : parentId,companyId,type);
		try {
			this.sendObjectToJson(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="treeForUser")
	public String treeForUser(Model model) {
		model.addAttribute("officeList", officeService.findAll());
		model.addAttribute("userList", systemService.findUser(new Page<User>(0,10000), new User()).getList());
		return "officeTree.jsp";
	}
	
	@RequestMapping(value="treeOffice")
	@ResponseBody
	public List<ZtreeVo> treeOffice(){
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Map<String, Object> result = new HashMap<String, Object>();
		//List<ProfessionType> list = professionTypeService.findAll();
//		List<IndustryType> list = industryTypeService.findAll();
//		//List<CustomerType> list = customerTypeService.findAll();
//		for(IndustryType o : list){
//		ZtreeVo z = new ZtreeVo(o.getId(), o.getParentId() !=null ? o.getParentId():0+"", o.getCodeName(),"1");
//		ztreeVos.add(z);
//	}
		
//		List<Area> areas = areaService.findAll();
//		for(Area o : areas){
//			ZtreeVo z = new ZtreeVo(o.getId(), o.getParentId() !=null ? o.getParentId():0+"", o.getName(),"1");
//			ztreeVos.add(z);
//		}
		List<Office> officeList = officeService.findAll();
		List<User> userList = systemService.findUser(new Page<User>(0,10000), new User()).getList();
		result.put("officeList", officeList);
		result.put("userList", userList);
		for(Office o : officeList){
			ZtreeVo z = new ZtreeVo(o.getId(), o.getParentId() !=null ? o.getParentId():0+"", o.getName(),"1");
			ztreeVos.add(z);
		}
		for(User u : userList){
			
			ZtreeVo z = new ZtreeVo(u.getId(), u.getOfficeId() !=null ? u.getOfficeId():0+"", u.getName(),"2");
			ztreeVos.add(z);
		}
		
		 
		return ztreeVos;
	}
	
	
	/**
	 * 设置部门中的编号
	 * @param office
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping("updateMaster")
	@ResponseBody
	public Map<String, Object> updateMaster(Office office, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		officeService.updateMaster(office);
		map.put("status", 1);
		map.put("message", "设置机构成功");
		return map;
	}
	/**
	 * 根据类型查询所有office
	 * @author luoyang
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "findAllType")
	@ResponseBody
	public List<Office> findAllType(String type){
		
		List<Office> list = officeService.findAllByType(type);
		
		return list;
	}
	/**
	 * 省管接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param parentId
	 * @param id
	 * @param type
	 * @param areaId
	 * @param code
	 * @param name
	 * @param grade
	 * @param address
	 * @param zipCode
	 * @param master
	 * @param phone
	 * @param fax
	 * @param email
	 * @param industry
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "setOfficeResult")
	@ResponseBody
	public String setOfficeResult(String OPFlag,String TimeStamp,String hashCode,String Summary,String parentId,String id,
			String type,String areaId,String code,String name,String grade,String address,String zipCode,
			String master,String phone,String fax,String email,String industry,Integer sort,String mark,Integer level){
		
		String resultCode = officeService.remoteOffice(OPFlag, TimeStamp, hashCode, Summary, parentId, id, type, areaId, code, name,
				grade, address, zipCode, master, phone, fax, email, industry, sort,mark,level);
		return resultCode;
	}


	/**
	 * 查询公司及其公司下的所有部门
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCorporateSectorMap")
	@ResponseBody
	public Map<String, List<Office>> getCorporateSectorMap(String companyId) throws Exception
	{
		return this.officeService.getCompanyAndOfficeList(companyId);
	}
	
}
