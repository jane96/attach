/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sccl.attech.common.config.Global;
import com.sccl.attech.common.mapper.JsonMapper;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.ITreeNode;
import com.sccl.attech.common.utils.StrUtils;
import com.sccl.attech.common.utils.TreeNodeUtils;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.DictService;
import com.sccl.attech.modules.sys.utils.DictUtils;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.sys.vo.DictVo;

/**
 * 字典Controller
 * 
 * @author sccl
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@ModelAttribute
	public Dict get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dictService.get(id);
		} else {
			return new Dict();
		}
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = { "list", "" })
	public String list(Dict dict, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {
		DictVo vo=new DictVo();
		
		// 前台传到后台被编码了两次，需要解码
		String searchLable = "";
		if (StringUtils.isNotBlank(dict.getLabel())) {
			searchLable = EncodedUtil.decodeValue(dict.getLabel());
			dict.setLabel(searchLable);
			vo.setLabel(searchLable);
		}
		String parentId = request.getParameter("parentId");
		String type = request.getParameter("dictType");
		if(null!=type&&!"".equals(type)){
		dict.setType(type);
		vo.setType(type);

		Page<Dict> page = dictService.find(new Page<Dict>(request, response),dict);
		model.addAttribute("page", page);
		// this.sendObjectToJson(page, response);

		 String dictJson = JsonMapper.getInstance().toJson(page);
		 
		 List<DictVo> list=dictService.findTree(vo);

		 String content = JsonMapper.getInstance().toJson(list);//json内容
		 String dictStart=dictJson.substring(0, dictJson.indexOf("["));
		 String dictEnd=dictJson.substring(dictJson.indexOf("]")+1);
		
		 response.setContentType("text/html;charset=UTF-8");
		 response.setCharacterEncoding("UTF-8");
		 PrintWriter pw = response.getWriter();
		 pw.write(dictStart+content+dictEnd);
		 pw.flush();
		 pw.close();

		 }
		return null;
	}

	@RequestMapping(value = { "dictTypeList" })
	public String dictTypeList(Dict dict, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		List<Dict> list = dictService.findDictType(dict);
		this.sendObjectToJson(list, response);
		return null;
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")
	// @Valid
	@ResponseBody
	public Map<String, Object> save(Dict dict, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (Global.isDemoMode()) {
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		// if (!beanValidator(model, dict)){
		// return form(dict, model);
		// }

		// ---------------------------增加数据的时候在父节点下面操作---------------------------------------------------
		if (null != request.getParameter("saveType")
				&& request.getParameter("saveType").equals("add")) {
			String saveId = request.getParameter("saveId");// 当前节点id
			String saveIds = request.getParameter("saveIds");// 父节点id组
			if (!"".equals(saveId)) {
				dict.setParentId(saveId);
//				dict.setParent(dict);
				dict.setParentIds(saveIds + "," + saveId);
			}

			String topId = request.getParameter("topId");// 顶级id
			if (!"".equals(topId) && "".equals(saveId)) {
				dict.setParentId(topId);
//				dict.setParent(dict);
				dict.setParentIds(topId);
			}

		}
		dictService.save(dict);
		// -------------------------------------------------------------------------------------------------
		resultMap.put("status", "1");
		resultMap.put("message", "字典编辑成功!");
		return resultMap;
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> delete(String id,
			RedirectAttributes redirectAttributes) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (Global.isDemoMode()) {
			resultMap.put("status", "-1");
			resultMap.put("message", "演示模式，不允许操作！");
			return resultMap;
		}
		dictService.delete(id);
		resultMap.put("status", "1");
		resultMap.put("message", "字典删除成功!");
		return resultMap;
	}

	/**
	 * 根据字典type查询字典集合信息
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "getDisctByType")
	@ResponseBody
	public List<Dict> getDisctByType(HttpServletRequest request,
			HttpServletResponse response, String type) {

		List<Dict> dictList = DictUtils.getDictList(type);

		return dictList;
	}

	// 导出文件
	@RequestMapping(value = "export")
	@ResponseBody
	public String exportFile(Dict dict, HttpServletRequest request,
			HttpServletResponse response) {
		String n = dictService.exportFile(dict, request, response);
		return n;
	}

	/**
	 * 公用获取树结构
	 */
	@RequestMapping(value = { "/treelist/{treeid}/type/{type}" }, method = RequestMethod.POST)
	@ResponseBody
	public List<AllTreeVo> tree(@PathVariable("treeid") String treeid,
			@PathVariable("type") String type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Dict dict = new Dict();
		User user = UserUtils.getUser();
		if (!"null".equals(StrUtils.null2String(type))) {
			dict.setType(type);
		}
		if (!"null".equals(StrUtils.null2String(treeid))) {
			dict.setId(treeid);
		}
		List<AllTreeVo> listAllTreeVos = dictService.getTreeList(dict, user
				.getOffice().getId());
		try {
			this.sendObjectToJson(listAllTreeVos, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取子节点
	 */
	@RequestMapping(value = { "/child/{type}" }, method = RequestMethod.GET)
	@ResponseBody
	public Object getPropType(@PathVariable("type") String type,String name,
			HttpServletResponse response) {
		User user = UserUtils.getUser();
		Dict dict = new Dict();
		if (!"null".equals(StrUtils.null2String(type))) {
			dict.setType(type);
		}
		List<AllTreeVo> list = dictService.getAllChildTreeList(dict, user
				.getOffice().getId(),name);
		try {
			this.sendObjectToJson(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取子节点
	 */
	@RequestMapping(value = { "/child1/{type}" }, method = RequestMethod.GET)
	@ResponseBody
	public Object getPropType1(@PathVariable("type") String type,String name,
			HttpServletResponse response) {
		User user = UserUtils.getUser();
		Dict dict = new Dict();
		if (!"null".equals(StrUtils.null2String(type))) {
			dict.setType(type);
		}
		List<AllTreeVo> list = dictService.getAllChildTreeList1(dict, user
				.getOffice().getId(),name);
		try {
			this.sendObjectToJson(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 根据IDS获取DICT
	 */
	@RequestMapping(value = { "/getPropTypeByIds" }, method = RequestMethod.GET)
	@ResponseBody
	public Object getPropTypeByIds(String ids,
			HttpServletResponse response) {
		
		List<AllTreeVo> list = dictService.getPropTypeByIds(ids);
		try {
			this.sendObjectToJson(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
