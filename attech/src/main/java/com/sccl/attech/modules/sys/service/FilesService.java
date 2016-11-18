/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.CacheUtils;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StrUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.AllTreeVo;
import com.sccl.attech.modules.sys.dao.DictDao;
import com.sccl.attech.modules.sys.dao.FilesDao;
import com.sccl.attech.modules.sys.dao.FilesRolesDao;
import com.sccl.attech.modules.sys.dao.GroupDao;
import com.sccl.attech.modules.sys.dao.GroupUserDao;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.FilesEntity;
import com.sccl.attech.modules.sys.entity.FilesRole;
import com.sccl.attech.modules.sys.entity.Grouping;
import com.sccl.attech.modules.sys.entity.GroupingUsers;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.DictUtils;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.sys.vo.FilesVo;

/**
 * 文档管理Service
 * @author 肖力
 * @version 2015-10-29
 */
@Service
@Transactional(readOnly = true)
public class FilesService extends BaseService {

	@Autowired
	private FilesDao filesDao;
	
	@Autowired
	private FilesRolesDao filesRolesDao;
	
	@Autowired
	private GroupUserDao groupUserDao;
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private MyBatisDictDao myBatisDictDao;
	
	public FilesEntity get(String id) {
		// MyBatis 查询
//		return myBatisDictDao.get(id);
		// Hibernate 查询
		return filesDao.get(id);
	}
	
	public Page<Grouping> findGroup(Page<Grouping> page, Grouping group) {
		
		
		return null;
	}
	
	public User findUser(String id) {
		List list=null;
		if(null!=id&&!"".equals(id)){
			list=userDao.find("from User where id='"+id+"'");
		}
		
		
		return (User)list.get(0);
	}
	
	
	
	@Transactional(readOnly = false)
	public void saveGroupUser(GroupingUsers group) {
		groupUserDao.save(group);
		
	}
	
	@Transactional(readOnly = false)
	public void saveFilesRoles(FilesRole filesRole) {
		filesRolesDao.save(filesRole);
		
	}
	
	@Transactional(readOnly = false)
	public List<GroupingUsers> findGroup(String id) {
		
		return groupUserDao.find("from GroupingUsers where userId='"+id+"'");
	}
	
	@Transactional(readOnly = false)
	public String save(FilesEntity filesEntity) {
		String id=filesDao.saveRet(filesEntity);
		
		return id;
	}
	
	@Transactional(readOnly = false)
	public List<FilesVo> filesList(String roleIds,String fileType) {
		String fileRole="";
		if(!"".equals(fileType)){
			fileRole="and t3.file_type='"+fileType+"'";
		}
		List<Map<String, Object>> mapList=filesDao.mapBySql("select t2.id id, t4.name userName,t2.FILE_TYPE fileType,t1.SUFFIX SUFFIX,t1.FILESIZE fileSize,t1.PATH path,t2.file_name fileName,t2.create_date fileTime from sys_files t2 left join sys_attach t1 on t1.REFERENCE_ID=t2.id left join SYS_FILES_ROLE t3 on t3.files_id=t2.id left join sys_user t4 on t4.id=t2.create_by where t1.business_type='sys_files' and t3.ROLE_IDS like '%"+roleIds+"%' "+fileRole+" and t2.del_flag='0' order by fileTime desc",null);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<FilesVo> list=new ArrayList<FilesVo>();
		for(int a=0;a<mapList.size();a++){
			FilesVo fileVo=new FilesVo();
			fileVo.setFileTime(sdf.format(mapList.get(a).get("FILETIME")));
			fileVo.setFileName(mapList.get(a).get("FILENAME").toString());
			fileVo.setPath(mapList.get(a).get("PATH").toString());
			fileVo.setUserName(mapList.get(a).get("USERNAME").toString());
			fileVo.setFileType(mapList.get(a).get("FILETYPE").toString());
			fileVo.setFileSize(mapList.get(a).get("FILESIZE").toString());
			fileVo.setSuffix(mapList.get(a).get("SUFFIX").toString());
			fileVo.setId(mapList.get(a).get("ID").toString());
			list.add(fileVo);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void deleteGroupUser(String groupId) {
		Parameter parameter=new Parameter(groupId);
		groupUserDao.updateBySql("delete from sys_grouping_users where grouping_id=:p1", parameter);
		
	}
	
	@Transactional(readOnly = false)
	public void deleteFiles(String id) {
		
		Parameter parameter=new Parameter(id);
		filesDao.updateBySql("update SYS_FILES set DEL_FLAG=1 where id=:p1",parameter);
		
	}
	
	@Transactional(readOnly = false)
	public void deleteFileRoles(String id) {
		
		Parameter parameter=new Parameter(id);
		filesDao.updateBySql("update SYS_FILES_ROLE set DEL_FLAG=1 where FILES_ID=:p1",parameter);
		
	}
	
	@Transactional(readOnly = false)
	public void deleteAttach(String id) {
		
		Parameter parameter=new Parameter(id);
		filesDao.updateBySql("update sys_attach set DEL_FLAG=1 where REFERENCE_ID=:p1",parameter);
		
	}
}
