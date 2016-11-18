/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sccl.attech.common.persistence.BaseEntity;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.Grouping;
import com.sccl.attech.modules.sys.entity.GroupingUsers;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.DictService;
import com.sccl.attech.modules.sys.service.GroupService;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * 
 * @author sccl
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/group")
public class GroupController extends BaseController {
	@Autowired
	private GroupService groupService;

	// get

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Grouping get(@PathVariable("id") String id) {
		
		logger.info("获取人员信息id=" + id);
		if (StringUtils.isNotBlank(id)) {
			Grouping group=groupService.get(id);
			Grouping groupUser=groupService.findGroupUser(id);
			if(null!=groupUser.getGroupUsers()&&!"".equals(groupUser.getGroupUsers())){
				group.setGroupUsers(groupUser.getGroupUsers().substring(0, groupUser.getGroupUsers().lastIndexOf(",")));
				group.setGroupUsersId(groupUser.getGroupUsersId().substring(0, groupUser.getGroupUsersId().lastIndexOf(",")));
			}
			
			System.out.println(group.getGroupUsers()+":"+group.getGroupUsersId());
			return group;
		} else {
			return new Grouping();
		}
	}

	// list
	@RequestMapping(value = "/list/page", method = RequestMethod.GET)
	public @ResponseBody
	Page page(Grouping group, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		Page<Grouping> page = groupService.findGroup(new Page<Grouping>(
				request, response), group);
		// SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < page.getList().size(); i++) {

			if (page.getList().get(i).getType().equals(BaseEntity.PUBLIC_GROUP)) {
				page.getList().get(i).setTypeName("公有");
			} else if (page.getList().get(i).getType()
					.equals(BaseEntity.PRIVATE_GROUP)) {
				page.getList().get(i).setTypeName("私有");
			}

			page.getList()
					.get(i)
					.setCreateDate_format(
							sft.format(page.getList().get(i).getCreateDate()));
			page.getList()
					.get(i)
					.setUpdateDate_format(
							sft.format(page.getList().get(i).getUpdateDate()));

			User user = groupService.findUser(page.getList().get(i)
					.getCreateBy());

			page.getList().get(i).setCreateName(user.getName());

		}
		return page;
	}

	// save
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	Object save(@RequestBody Grouping grouping, HttpServletRequest request) {

		String userIds = request.getParameter("userIds");

		Grouping group = new Grouping();
		group.setName(grouping.getName());
		group.setCompanyId(UserUtils.getUser().getCompany().getId());
		group.setOffiecId(UserUtils.getUser().getOfficeId());
		group.setCreateBy(UserUtils.getUser().getId());
		if(null!=grouping.getType()){
			group.setType(grouping.getType());
		}else{
			group.setType("0");
		}
		group.setSort(grouping.getSort());
		group.setCreateDate(new Date());
		group.setUpdateDate(new Date());
		group.setDelFlag("0");
		String id = groupService.save(group);

		if (null != userIds && !"".equals(userIds)) {
			String userIdArray[] = userIds.split(",");

			for (int i = 0; i < userIdArray.length; i++) {
				System.out.println(userIdArray[i]);
				GroupingUsers users = new GroupingUsers();
				users.setGroupingId(id);
				users.setUserId(userIdArray[i]);
				groupService.saveGroupUser(users);
			}
		}

		logger.info("注册人员信息成功id=");
		return actionSuccess("新增成功");
	}

	// update
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	Object update(@PathVariable("id") String id,
			@RequestBody Grouping grouping, HttpServletRequest request) {
		logger.info("更新人员信息id=" + grouping.getName() + "=" + id);
		
		grouping.setUpdateDate(new Date());//修改时间
		grouping.setUpdateBy(UserUtils.getUser().getId());//修改人
		
		groupService.updateGroup(id,grouping);
		
		
		groupService.deleteGroupUser(id);
		
		String userIds = request.getParameter("userIds");
		
		if (null != userIds && !"".equals(userIds)) {
			String userIdArray[] = userIds.split(",");

			for (int i = 0; i < userIdArray.length; i++) {
				
				GroupingUsers users = new GroupingUsers();
				users.setGroupingId(id);
				users.setUserId(userIdArray[i]);
				groupService.saveGroupUser(users);
			}
		}
		
		return actionSuccess("更新成功");
	}

	// delete
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	Object delete(@PathVariable("id") String id) {
		System.out.println("deleteId=" + id);
		logger.info("删除人员信息id=" + id);
		
		groupService.delete(id);
		
		return actionSuccess("删除成功");
	}

}
