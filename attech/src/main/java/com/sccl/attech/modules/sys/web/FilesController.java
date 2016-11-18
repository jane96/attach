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
import com.sccl.attech.modules.sys.entity.FilesEntity;
import com.sccl.attech.modules.sys.entity.FilesRole;
import com.sccl.attech.modules.sys.entity.Grouping;
import com.sccl.attech.modules.sys.entity.GroupingUsers;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.DictService;
import com.sccl.attech.modules.sys.service.FilesService;
import com.sccl.attech.modules.sys.service.GroupService;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.sys.vo.FilesVo;

/**
 * 文档管理Controller
 * 
 * @author 肖力
 * @version 2015-10-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/files")
public class FilesController extends BaseController {
	@Autowired
	private FilesService filesService;
	
	@Autowired
	private GroupService groupService;

	// save
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	Object save(@RequestBody String fileRole, HttpServletRequest request) {
		
		String fileUrl = request.getParameter("fileName");//文件路径

		FilesEntity filesVo = new FilesEntity();
		
		
		filesVo.setCompanyId(UserUtils.getUser().getCompany().getId());//公司id
		filesVo.setOffiecId(UserUtils.getUser().getOfficeId());//部门id
		filesVo.setCreateBy(UserUtils.getUser().getId());//创建人
		filesVo.setUpdateBy(UserUtils.getUser().getId());//更新人
		filesVo.setCreateDate(new Date());//创建时间
		filesVo.setUpdateDate(new Date());//更新时间
		String name=fileUrl.replace("\\", "/");//替换路径的斜杠
		filesVo.setFileName(name.substring(name.lastIndexOf("/")+1, name.lastIndexOf(".")));//获取文件名
		filesVo.setFileRole(fileRole);//文件可见域（0-自己，1-部门，2-群组/分组）
		
		String fileType=fileUrl.substring(fileUrl.lastIndexOf(".")+1);//获取文件后缀名
		
		if(fileType.equals("jpg")||fileType.equals("png")||fileType.equals("gif")){//判断文件类型
			filesVo.setFileType("1");//图片
		}else{
			filesVo.setFileType("0");//文件
		}
		
		
		
		filesVo.setDelFlag("0");//删除标记，0表示文件没被删除
		
		FilesRole filesRole = new FilesRole();
		
		
		String id = filesService.save(filesVo);//保存数据并返回文件id
		
		
		filesRole.setFilesId(id);//文件id
		filesRole.setDelFlag("0");//删除标记
		filesRole.setFileType(fileRole);//文件可见域
		
		if(fileRole.equals("0")){//可见域为0存入用户id
			filesRole.setRoleIds(UserUtils.getUser().getId());
		}
		if(fileRole.equals("1")){//可见域为1存入部门id
			filesRole.setRoleIds(UserUtils.getUser().getOfficeId());
		}
		if(fileRole.equals("2")){//可见域为2存入群组id
			List<GroupingUsers> groupingUsers=filesService.findGroup(UserUtils.getUser().getId());//根据用户id查询当前用户的所属群组
			String groups="";
			if(groupingUsers.size()>0){//如果当前用户有群组就循环拼装用户的群组id
				
				for(int i=0;i<groupingUsers.size();i++){
					groups+=groupingUsers.get(i).getGroupingId()+",";
				}
				groups.substring(0, groups.lastIndexOf(","));
			}
			
			filesRole.setRoleIds(groups);//群组ids
		}
		
		
		
		filesService.saveFilesRoles(filesRole);//把数据存入文件权限表
		
		
		

		logger.info("注册人员信息成功id=");
		return actionSuccess(id);
	}

	// list
	@RequestMapping(value = "/fileList", method = RequestMethod.POST)
	public @ResponseBody
	Object fileList(@RequestBody String id, HttpServletRequest request) {
		
		
		
		String roleIds=request.getParameter("roleIds");//文件可见域（0-自己，1-部门，2-群组/分组）
		
		List<FilesVo> list=null;
		if(null!=roleIds&&!"".equals(roleIds)){//如果可见域有值就根据可见域查询文件列表
			if(!roleIds.equals("3")){//3表示查询当前用户有权限查看的所有文件
				String ids="";
				if(roleIds.equals("0")){//0表示查询当前用户的私有文件
					ids=UserUtils.getUser().getId();
				}
				
				if(roleIds.equals("1")){//1表示查询用户所属部门的文件
					ids=UserUtils.getUser().getOfficeId();
				}
				
				if(roleIds.equals("2")){//2表示查询用户所属群组的文件
					List<GroupingUsers> groups=filesService.findGroup(UserUtils.getUser().getId());
					if(groups.size()>0){
						ids=filesService.findGroup(UserUtils.getUser().getId()).get(0).getGroupingId();//查询用户所属群组
					}else{
						ids="noGroup";
					}
				}
				list=filesService.filesList(ids,roleIds);//根据可见域查询文件
			}else{
				list=filesService.filesList("","");//查询用户所能访问的所有文件
			}
		}else{
			list=filesService.filesList("","");//查询用户所能访问的所有文件
		}
		
		
		String fileJson="";
		String imgeJson="";
		int all=filesService.filesList("","").size();//用于前台统计文件总数
		int office=filesService.filesList(UserUtils.getUser().getOfficeId(),"1").size();//用于前台统计部门文件数
		int my=filesService.filesList(UserUtils.getUser().getId(),"0").size();//用于前台统计用户私有文件数
		int isGroup=0;//用来判断用户是否加入过群组
		String groupId="";
		List<GroupingUsers> groups=filesService.findGroup(UserUtils.getUser().getId());
		if(groups.size()>0){//如果用户加入了群组就根据用户id查找群组id
			isGroup=1;//加入了群组
			groupId=filesService.findGroup(UserUtils.getUser().getId()).get(0).getGroupingId();//查询群组id
		}else{
			isGroup=0;//没有加入群组
			groupId="noGroup";
		}
		int group=filesService.filesList(groupId,"2").size();//用于前台统计群组文件数
		
		//-------用来判断循环出来的标题应该付给文件列表还是图片列表---------
		int fileTitleNumber=0;
		int imgeTitleNumber=0;
		//------------------------------------------------
		for(int a=0;a<list.size();a++){//循环并拼装json数组
			
			
			if(list.get(a).getFileType().equals("0")){
				
				fileJson+="{'id':'"+list.get(a).getId()+"','fileTitleTime':'"+list.get(a).getFileTime()+"','fileName':'"+list.get(a).getFileName()+"','path':'"+list.get(a).getPath()+"','suffix':'"+list.get(a).getSuffix()+"','fileType':'"+list.get(a).getFileType()+"','userName':'"+list.get(a).getUserName()+"','fileSize':'"+list.get(a).getFileSize()+"','fileTime':'"+list.get(a).getFileTime()+"'},";
			}else{
				
				imgeJson+="{'id':'"+list.get(a).getId()+"','imgeTitleTime':'"+list.get(a).getFileTime()+"','fileName':'"+list.get(a).getFileName()+"','path':'"+list.get(a).getPath()+"','suffix':'"+list.get(a).getSuffix()+"','fileType':'"+list.get(a).getFileType()+"','userName':'"+list.get(a).getUserName()+"','fileSize':'"+list.get(a).getFileSize()+"','fileTime':'"+list.get(a).getFileTime()+"'},";
			}
			
			
		}
		
		String jsonList="";
		String imgeList="";
		if(!"".equals(fileJson)){
			jsonList=fileJson.substring(0, fileJson.lastIndexOf(","));
		}
		if(!"".equals(imgeJson)){
			imgeList=imgeJson.substring(0, imgeJson.lastIndexOf(","));
		}
		
		return "{'isGroup':'"+isGroup+"','all':'"+all+"','office':'"+office+"','my':'"+my+"','group':'"+group+"','fileList':["+jsonList+"],'imgeList':["+imgeList+"]}";
	}

	// delete
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Object delete(@RequestBody String id, HttpServletRequest request) {
		String fileId=request.getParameter("fileId");
		logger.info("删除文件信息id=" + id);
		
		filesService.deleteFiles(fileId);
		filesService.deleteFileRoles(fileId);
		filesService.deleteAttach(fileId);
		
		return actionSuccess("删除成功");
	}

}
