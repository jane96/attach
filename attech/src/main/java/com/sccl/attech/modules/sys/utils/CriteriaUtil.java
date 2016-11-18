/**
 * @(#)CriteriaUtil.java     1.0 2015-6-30 14:27:05
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.sys.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.CharUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.sccl.attech.common.persistence.DataEntity;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.Office;

/**
 * The Class CriteriaUtil.
 * 查询条件工具类
 * @author zzz
 * @version 1.0,2015-6-30 14:27:05
 * @see com.sccl.attech.modules.sys.utils
 * @since JDK1.7
 */
public class CriteriaUtil {
	
	public static void createDateCond(DetachedCriteria dc,DataEntity<?> entity) {
		if (null == entity)
			return;
		if(null!=entity.getUpdateDateStart())
			dc.add(Restrictions.ge("updateDate",entity.getUpdateDateStart()));
		if(null!=entity.getUpdateDateEnd())
			dc.add(Restrictions.le("updateDate",entity.getUpdateDateEnd()));
	}
	/**
	 * 创建 project cond.
	 * 创建公司或部门查询条件
	 * @param dc the dc
	 * @param projectInfo the project info
	 */
	public static void createOfficeCond(DetachedCriteria dc, Office office,boolean isCompany) {
		if (null == office)
			return;
		if(isCompany){
			dc.createAlias("company", "company");
			if(StringUtils.isNotBlank(office.getId()))
				dc.add(Restrictions.eq("company.id", office.getId()));
		}else{
			dc.createAlias("office", "office");
			if(StringUtils.isNotBlank(office.getId()))
				dc.add(Restrictions.eq("office.id", office.getId()));
		}
	}

	

}
