/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.web;

import java.io.IOException;

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

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.web.BaseController;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.SystemService;

/**
 * 用户Controller
 * @author sccl
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/demo")
public class DemoController extends BaseController {
	
	//list
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "/user/list/page", method = RequestMethod.GET)  
	public @ResponseBody  
	Page page(User user, HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		String jsonString = request.getParameter("searcher");
		String searchType = getJsonString(jsonString, "type");
		String searchKey = getJsonString(jsonString, "key");
		if(("4").equals(searchType)){//电话搜索
			user.setMobile(searchKey);
		}else if(("2").equals(searchType)){//姓名搜索
			user.setName(searchKey);
		}
		Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
	    return page;
	}

	//get
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)  
    public @ResponseBody  
    User get(@PathVariable("id") String id) {  
        logger.info("获取人员信息id=" + id);  
        if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
    }  
  
    //save
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)  
    public @ResponseBody  
    Object save(@RequestBody User user,HttpServletRequest request) {  
    	System.out.println(user.getName() + "=" + request.getParameter("key"));
        logger.info("注册人员信息成功id=");  
        return actionSuccess("新增成功"); 
    }  
  
    //update
    @RequestMapping(value = "/user/update/{id}", method = RequestMethod.PUT)  
    public @ResponseBody  
    Object update(@PathVariable("id") String id,@RequestBody User user,HttpServletRequest request) {  
        logger.info("更新人员信息id=" + user.getName() + "=" + id);  
        System.out.println(user.getName() + "=" + request.getParameter("key") + "==" + id);
        return actionSuccess("更新成功");
    }  
    
    //delete
    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)  
    public @ResponseBody  
    Object delete(@PathVariable("id") String id) {
    	System.out.println("deleteId=" + id);
        logger.info("删除人员信息id=" + id);  
        return actionSuccess("删除成功"); 
    }  
  
//    @RequestMapping(value = "/user", method = RequestMethod.PATCH)  
//    public @ResponseBody  
//    List<Person> listPerson(@RequestParam(value = "name", required = false, defaultValue = "") String name) {  
//  
//        logger.info("查询人员name like " + name);  
//        List<Person> lstPersons = new ArrayList<Person>();  
//  
//        Person person = new Person();  
//        person.setName("张三");  
//        person.setSex("男");  
//        person.setAge(25);  
//        person.setId(101);  
//        lstPersons.add(person);  
//  
//        Person person2 = new Person();  
//        person2.setName("李四");  
//        person2.setSex("女");  
//        person2.setAge(23);  
//        person2.setId(102);  
//        lstPersons.add(person2);  
//  
//        Person person3 = new Person();  
//        person3.setName("王五");  
//        person3.setSex("男");  
//        person3.setAge(27);  
//        person3.setId(103);  
//        lstPersons.add(person3);  
//  
//        return lstPersons;  
//    }  
//	@RequiresPermissions("sys:user:view")
//	@RequestMapping(value = "/person/list", method = RequestMethod.GET)  
//	public @ResponseBody  
//	List<Person> list(@RequestParam(value = "pageNo", required = true, defaultValue = "") String pageNo,HttpServletRequest request) throws IOException {
//        System.out.println(request.getParameter("pageNo") + "==" + request.getParameter("name"));
//        logger.info("查询人员name like " + pageNo);  
//        List<Person> lstPersons = new ArrayList<Person>();  
//  
//        Person person = new Person();  
//        person.setName("张三");  
//        person.setSex("男");  
//        person.setAge(25);  
//        person.setId(101);  
//        lstPersons.add(person);  
//  
//        Person person2 = new Person();  
//        person2.setName("李四");  
//        person2.setSex("女");  
//        person2.setAge(23);  
//        person2.setId(102);  
//        lstPersons.add(person2);  
//  
//        Person person3 = new Person();  
//        person3.setName("王五");  
//        person3.setSex("男");  
//        person3.setAge(27);  
//        person3.setId(103);  
//        lstPersons.add(person3);  
//  
//        return lstPersons;
//	}
//	
//	@RequiresPermissions("sys:user:view")
//	@RequestMapping(value = "/user/list", method = RequestMethod.GET)  
//	public @ResponseBody  
//	String list(User user, HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
//		Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
//		System.out.println(request.getParameter("pageNo") + "==" + request.getParameter("name"));
//	    model.addAttribute("page", page);
//	    this.sendObjectToJson(page, response);
//	    return null;
//	}
	
//	@RequiresPermissions("sys:user:view")
//	@RequestMapping(value = "/user/list/page/get", method = RequestMethod.GET)  
//	public @ResponseBody  
//	List<User> getList(User user, HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
//		String jsonString = request.getParameter("searcher");
//		String searchType = getJsonString(jsonString, "type");
//		String searchKey = getJsonString(jsonString, "key");
//		if(("4").equals(searchType)){//电话搜索
//			user.setMobile(searchKey);
//		}else if(("2").equals(searchType)){//姓名搜索
//			user.setName(searchKey);
//		}
//		Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
//		return page.getList();
//	}
	
	
    @Autowired
	private SystemService systemService;
}
