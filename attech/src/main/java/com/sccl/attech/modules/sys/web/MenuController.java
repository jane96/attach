/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.io.StringWriter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.FreeMarkerUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Menu;
import com.sccl.attech.modules.sys.entity.Vote;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 菜单Controller
 * 
 * @author sccl
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SystemService	systemService;
	
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getMenu(id);
		} else {
			return new Menu();
		}
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "name", required=false) String name,
			Menu menu, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*
		 * List<Menu> list = Lists.newArrayList(); List<Menu> sourcelist = systemService.findAllMenu(); Menu.sortList(list, sourcelist, "1"); model.addAttribute("list", list);
		 * 
		 * Page<Menu> page = new Page<Menu>(request, response); page.setList(list); this.sendObjectToJson(page, response);
		 */
		//前台传到后台被编码了两次，需要解码
		
		
		if(StringUtils.isNotBlank(menu.getName())){
			String searchLable = EncodedUtil.decodeValue(menu.getName());
			menu.setName(searchLable);
		}
		Page<Menu> page = systemService.find(new Page<Menu>(request, response), menu);
		model.addAttribute("page", page);
		this.sendObjectToJson(page, response);
		return null;
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		if (menu.getParent() == null || menu.getParent().getId() == null) {
			menu.setParent(new Menu("1"));
		}
		menu.setParent(systemService.getMenu(menu.getParent().getId()));
		model.addAttribute("menu", menu);
		return "modules/sys/menuForm";
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
		
		// if(Global.isDemoMode()){
		// addMessage(redirectAttributes, "演示模式，不允许操作！");
		// return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		// }
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		menu.setParent(new Menu(menu.getParentId()));
		System.out.println("ids:" + menu.getId());
		if (!beanValidator(model, menu)) {
			// return form(menu, model);
			map.put("status", 0);
			map.put("message", "数据有误！");
			return map;
		}
		systemService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		map.put("status", 1);
		map.put("message", "保存菜单'" + menu.getName() + "'成功");
		return map;
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		// if(Global.isDemoMode()){
		// addMessage(redirectAttributes, "演示模式，不允许操作！");
		// return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		if (Menu.isRoot(id)) {
			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");

			map.put("status", 0);
			map.put("message", "删除菜单失败, 不允许删除顶级菜单或编号为空");
		} else {
			systemService.deleteMenu(id);
			addMessage(redirectAttributes, "删除菜单成功");

			map.put("status", 1);
			map.put("message", "删除菜单成功");
		}
		return map;
	}

	@RequiresUser
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menuTree";
	}


	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/menu/";
		}
		int len = ids.length;
		Menu[] menus = new Menu[len];
		for (int i = 0; i < len; i++) {
			menus[i] = systemService.getMenu(ids[i]);
			menus[i].setSort(sorts[i]);
			systemService.saveMenu(menus[i]);
		}
		addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + Global.getAdminPath() + "/sys/menu/";
	}

	/**
	 * @param extId
	 * @param id
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<AllTreeVo> treeData(@RequestParam(required = false) String extId, @RequestParam(required = true) String id, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		AllTreeVo allTreeVo = systemService.getMenuTree(null, systemService.getMenu(id), extId);
		List<AllTreeVo> areaTreeList = Lists.newArrayList();
		areaTreeList.add(allTreeVo);
		return areaTreeList;
	}

	/**
	 * 菜单复选框数据
	 * 
	 * @param extId 菜单id
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "menuCheckBoxData")
	public List<Map<String, Object>> menuCheckBoxData(@RequestParam(required = false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		return systemService.findMenuByParentId();
	}

	@ResponseBody
	@RequestMapping(value = "file/{type}")
	public String getMenuFile(@PathVariable int type,HttpServletRequest request) {
		
		//初始化freemarker需要的数据
		Map<String, Object> model = Maps.newHashMap();
		model.put("menus", UserUtils.getMenuList());
		model.put("user", UserUtils.getUser());
		StringWriter result =new StringWriter();
		//模板路径
		String tplFile="pages/partials/nav.html";
		switch (type) {
		case 1:// 根据模板生成js
			tplFile="assets/js/config.router.js";
			break;
		default:// 根据模板生成html
			break;
		}
		FreeMarkerUtil.initConfig(request.getSession().getServletContext(), "/");
		FreeMarkerUtil.processTemplate(tplFile, model, result);
		return result.toString();
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(Menu menu, HttpServletRequest request, HttpServletResponse response) {
		String n=systemService.exportMenu(menu, request, response);
		return n;
	}
}
