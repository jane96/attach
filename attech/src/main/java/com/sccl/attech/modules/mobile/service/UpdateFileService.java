/**
 * @(#)UpdateFileService.java  2015-07-14  2015-07-14 15:59:09 中国标准时间
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.mobile.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.mobile.entity.UpdateFile;
import com.sccl.attech.modules.mobile.dao.UpdateFileDao;
 
/**
 * The App升级Service
 * 
 * @author zzz
 * @version 2015-07-14,2015-07-14 15:59:09 中国标准时间
 * @see com.sccl.attech.modules.mobile.web
 * @since JDK1.7
 */
@Component
@Transactional(readOnly = true)
public class UpdateFileService extends BaseService {

	@Autowired
	private UpdateFileDao updateFileDao;
	
	public UpdateFile get(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(String=" + id + ") - 开始");
		}
		UpdateFile updateFile=updateFileDao.get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("get(String=" + id + ") - 结束 - 返回值=" + updateFile);
		}
		return updateFile;
	}
	
	public Page<UpdateFile> find(Page<UpdateFile> page, UpdateFile updateFile) {
		if (logger.isDebugEnabled()) {
			logger.debug("find(Page<UpdateFile>=" + page + ", UpdateFile=" + updateFile + ") - 开始");
		}
		
		DetachedCriteria dc = updateFileDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(updateFile.getName())){
			dc.add(Restrictions.eq("name", updateFile.getName()));
		}
		if (StringUtils.isNotEmpty(updateFile.getVersion())){
			dc.add(Restrictions.eq("version", updateFile.getVersion()));
		}
		if (StringUtils.isNotEmpty(updateFile.getType())){
			dc.add(Restrictions.eq("type", updateFile.getType()));
		}
		if (StringUtils.isNotEmpty(updateFile.getPath())){
			dc.add(Restrictions.eq("path", updateFile.getPath()));
		}
		if (StringUtils.isNotEmpty(updateFile.getUrl())){
			dc.add(Restrictions.eq("url", updateFile.getUrl()));
		}
		dc.add(Restrictions.eq(UpdateFile.FIELD_DEL_FLAG, UpdateFile.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		Page<UpdateFile> returnPage =updateFileDao.find(page, dc);
		
		if (logger.isDebugEnabled()) {
			logger.debug("find(Page<UpdateFile>=" + page + ", UpdateFile=" + updateFile + ") - 结束 - 返回值=" + page);
		}
		return returnPage;
	}
	
	@Transactional(readOnly = false)
	public void save(UpdateFile updateFile) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(UpdateFile=" + updateFile + ") - 开始");
		}
		updateFileDao.save(updateFile);
		if (logger.isDebugEnabled()) {
			logger.debug("save(UpdateFile=" + updateFile + ") - 结束");
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 开始");
		}
		updateFileDao.deleteById(id);
		
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 结束");
		}
	}
	
	/**
	 * 获取最新的app下载包路径
	 * @return
	 */
	public String find() {
		UpdateFile file = new UpdateFile();
		Page<UpdateFile> page = new Page<UpdateFile>();
		page.setPageSize(-1);
		UpdateFile updateFile = new UpdateFile();
		Page<UpdateFile> returnPage = find(page,updateFile);
		if(returnPage!=null&&returnPage.getList()!=null&&returnPage.getList().size()>0) 
			file = returnPage.getList().get(0);
		return file!=null?file.getUrl():"";
	}
}
