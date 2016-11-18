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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.service.AreaService;
import com.sccl.attech.modules.sys.utils.DictUtils;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * @author sccl
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if(StringUtils.isNotBlank(area.getName())){
			String searchLable = EncodedUtil.decodeValue(area.getName());
			area.setName(searchLable);
		}
		
		
        Page<Area> page = areaService.findArea(new Page<Area>(request, response), area); 
        model.addAttribute("page", page);
        this.sendObjectToJson(page, response);
        return null;
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent()==null||area.getParent().getId()==null){
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}
		area.setParent(areaService.get(area.getParent().getId()));
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(Area area, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			//addMessage(redirectAttributes, "演示模式，不允许操作！");
			//return "redirect:"+Global.getAdminPath()+"/sys/area";
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		Area parent = new Area();
		parent.setId(area.getParentId());
		area.setParent(parent);
		areaService.save(area);
		
		resultMap.put("status", "1");
		resultMap.put("message", "区域编辑成功!");
		return resultMap;
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id, RedirectAttributes redirectAttributes) {
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(Global.isDemoMode()){
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		if (Area.isAdmin(id)){
			resultMap.put("status", "-2");
			resultMap.put("message", "删除区域失败, 不允许删除顶级区域或编号为空！");
			return resultMap;
		}else{
			areaService.delete(id);
			resultMap.put("status", "1");
			resultMap.put("message", "删除区域成功！");
		}
		return resultMap;
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData1")
	public List<Map<String, Object>> treeData1(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取type的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "getAreaType")
	public String getAreaType(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 List<Dict> dictList = DictUtils.getDictList("sys_area_type");
		 this.sendObjectToJson(dictList, response);
	     return null;
	}
	
	
	
//	/**
//	 * 区域树
//	 * @param extId 当前区域id
//	 * @param id 根节点区域id
//	 * @param response
//	 * @return
//	 */
//	@RequiresUser 
//	@ResponseBody
//	@RequestMapping(value = "areaTreeData")
//	public List<AllTreeVo> treeData(@RequestParam(required=false) String extId,@RequestParam(required=true) String id, HttpServletResponse response) {
//		response.setContentType("application/json; charset=UTF-8");
//		AllTreeVo allTreeVo = new AllTreeVo();
//		allTreeVo = areaService.getAreaTree(null, areaService.get(id), extId);		
//		List<AllTreeVo> areaTreeList = Lists.newArrayList();
//		areaTreeList.add(allTreeVo);
//		return areaTreeList;
//	}
	
	
	/**
	 * 根据parentId查询所有区域信息
	 * @author luoyang
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "findListByParentId")
	@ResponseBody
	public List<Area> findListByType(String type){
		
		List<Area> list = areaService.findListByType(type);
		
		return list;
	}
	
	//导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(Area area , HttpServletRequest request, HttpServletResponse response) {
		String n=areaService.exportFile(area, request, response);
		return n;
	}
	/**
	 * 省管接口
	 * @param OPFlag
	 * @param TimeStamp
	 * @param hashCode
	 * @param Summary
	 * @param parentId
	 * @param type
	 * @param code
	 * @param name
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "setAreaResult")
	@ResponseBody
	public String setAreaResult(String OPFlag,String TimeStamp,String hashCode,String Summary,String parentId,
			String type,String code,String name,String id,String parentType){
		//TODO 验证请求是否合法
		String resultCode = areaService.remoteArea(OPFlag, TimeStamp, hashCode, Summary, parentId, type, code,
				name, id, parentType);
		return resultCode;
	}


}
