/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aj.org.objectweb.asm.Type;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.modules.sys.dao.LogDao;
import com.sccl.attech.modules.sys.entity.Log;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 日志Service
 * @author sccl
 * @version 2013-6-2
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService {

	@Autowired
	private LogDao logDao;
	
	public Log get(String id) {
		return logDao.get(id);
	}
	
	public Page<Log> find(Page<Log> page, Map<String, Object> paramMap) {
		DetachedCriteria dc = logDao.createDetachedCriteria();

//		Long createById = StringUtils.toLong(paramMap.get("createById"));
//		if (createById > 0){
//			dc.add(Restrictions.eq("createBy.id", createById));
//		}
		String type= ObjectUtils.toString(paramMap.get("type"));
		if("0".equals(type)){//如果是省管接口日志则单独添加
			dc.add(Restrictions.eq("type", type));
		}else{
			String manageName = ObjectUtils.toString(paramMap.get("manageName"));
			if(StringUtils.isNotBlank(manageName)){
				dc.createAlias("createBy", "createBy");
				dc.add(Restrictions.like("createBy.name", "%"+EncodedUtil.decodeValue(manageName)+"%"));
			}
			dc.createAlias("company", "company");
			String companyId = ObjectUtils.toString(paramMap.get("companyId"));
			if (StringUtils.isNotBlank(companyId)){
				
				dc.add(Restrictions.eq("company.id",companyId));
			}
			
			String requestUri = ObjectUtils.toString(paramMap.get("requestUri"));
			if (StringUtils.isNotBlank(requestUri)){
				dc.add(Restrictions.like("requestUri", "%"+requestUri+"%"));
			}
			
			String exception = ObjectUtils.toString(paramMap.get("exception"));
			if (StringUtils.isNotBlank(exception)){
				dc.add(Restrictions.eq("type", Log.TYPE_EXCEPTION));
			}
			
			if (StringUtils.isNotBlank(type)){
				dc.add(Restrictions.eq("type", type));
			}
			
			Date beginDate = DateUtils.parseDate(paramMap.get("start"));
			if (beginDate == null){
				beginDate = DateUtils.setDays(new Date(), 1);
				paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
			}
			Date endDate = DateUtils.parseDate(paramMap.get("end"));
			if (endDate == null){
				endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
				paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
			}
			dc.createAlias("office","office");
			User currentUser = UserUtils.getUser();
			dc.add(Restrictions.or(dataScopeFilter(currentUser, "office", "createBy"),Restrictions.eq("type","0")));
			dc.add(Restrictions.between("createDate", beginDate, endDate));
		}
		dc.addOrder(Order.desc("createDate"));
		return logDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param paramMap
	 * @return
	 */
	public List<Log> getList(Map<String, Object> paramMap){
		DetachedCriteria dc = logDao.createDetachedCriteria();

		Long createById = StringUtils.toLong(paramMap.get("createById"));
		if (createById > 0){
			dc.add(Restrictions.eq("createBy.id", createById));
		}
		String manageName = ObjectUtils.toString(paramMap.get("manageName"));
		if(StringUtils.isNotBlank(manageName)){
			dc.createAlias("createBy", "createBy");
			dc.add(Restrictions.like("createBy.name", EncodedUtil.decodeValue(manageName)));
		}
		
		String requestUri = ObjectUtils.toString(paramMap.get("requestUri"));
		if (StringUtils.isNotBlank(requestUri)){
			dc.add(Restrictions.like("requestUri", "%"+requestUri+"%"));
		}

		String exception = ObjectUtils.toString(paramMap.get("exception"));
		if (StringUtils.isNotBlank(exception)){
			dc.add(Restrictions.eq("type", Log.TYPE_EXCEPTION));
		}
		
		Date beginDate = DateUtils.parseDate(paramMap.get("start"));
		if (beginDate == null){
			beginDate = DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		Date endDate = DateUtils.parseDate(paramMap.get("end"));
		if (endDate == null){
			endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		dc.add(Restrictions.between("createDate", beginDate, endDate));
		
		dc.addOrder(Order.desc("createDate"));
		return logDao.find(dc);
	}
	/**
	 * 根据数字类型的日志类型获取中文的日志类型，用于导出数据时显示
	 * @param type
	 * @return
	 */
	//1：接入日志；2：异常日志；3：编辑日志；4：修改日志；5：删除日志；6：登录日志；7：登出日志；8：导出日志；9：权限配置；
	public String getType(String type){
		String typeValue="   ";
		if(type != null){
			if(type.equals("1")){
				typeValue="接入日志";
			}
			else if(type.equals("2")){
				typeValue="异常日志";
			}
			else if(type.equals("3")){
				typeValue="编辑日志";
			}
			else if(type.equals("4")){
				typeValue="修改日志";
			}
			else if(type.equals("5")){
				typeValue="删除日志";
			}
			
			else if(type.equals("6")){
				typeValue="登录日志";
			}
			else if(type.equals("7")){
				typeValue="登出日志";
			}
			else if(type.equals("8")){
				typeValue="导出日志";
			}
			else if(type.equals("9")){
				typeValue="权限配置";
			}
		}
		
		return typeValue;		
	}
	
	//导出文件
	public String exportFile(@RequestParam Map<String, Object> paramMap , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"log.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "归属公司");
			e.setTitleCell(2, "归属部门");	
			e.setTitleCell(3, "操作用户");	
			e.setTitleCell(4, "URI");	
			e.setTitleCell(5, "提交方式");	
			e.setTitleCell(6, "日志类型");	
			e.setTitleCell(7, "操作者IP");
			e.setTitleCell(8, "创建者");	
			e.setTitleCell(9, "创建时间");	
			e.setTitleCell(10, "用户代理");	
			e.setTitleCell(11, "提交参数");
			e.setTitleCell(12, "异常信息");				
			List<Log> list=getList(paramMap);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					if(list.get(i-1).getCompanyName() == null){
						e.setCell(1, "  ");
					}else{
						e.setCell(1, list.get(i-1).getCompanyName());
					}
					if(list.get(i-1).getOfficeName() == null){
						e.setCell(2, "  ");
					}else{
						e.setCell(2, list.get(i-1).getOfficeName());
					}
					if(list.get(i-1).getOperationName() == null){
						e.setCell(3, "  ");
					}else{
						e.setCell(3, list.get(i-1).getOperationName());
					}
					if(list.get(i-1).getRequestUri() == null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getRequestUri());
					}
					if(list.get(i-1).getMethod() == null){
						e.setCell(5, "  ");
					}else{
						e.setCell(5, list.get(i-1).getMethod());
					}										
					String type=list.get(i-1).getType();
					String typeValue=getType(type);
					e.setCell(6, typeValue);
					if(list.get(i-1).getRemoteAddr() == null){
						e.setCell(7, "  ");
					}else{
						e.setCell(7, list.get(i-1).getRemoteAddr());
					}
					if(list.get(i-1).getCreateBy() == null){
						e.setCell(8, "  ");
					}else{
						e.setCell(8, list.get(i-1).getCreateBy().getName());
					}
					
					if(list.get(i-1).getCreateDate() == null){
						e.setCell(9, "  ");
					}else{
						e.setCell(9, list.get(i-1).getCreateDate());
					}
					if(list.get(i-1).getUserAgent() == null){
						e.setCell(10, "  ");
					}else{
						e.setCell(10, list.get(i-1).getUserAgent());
					}
					if(list.get(i-1).getParams() == null){
						e.setCell(11, "  ");
					}else{
						e.setCell(11, list.get(i-1).getParams());
					}
					if(list.get(i-1).getException() == null){
						e.setCell(12, "  ");
					}else{
						e.setCell(12, list.get(i-1).getException());
					}
					
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}

	
}
