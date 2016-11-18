/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.sccl.attech.modules.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.UserOffice;

@Repository
public class UserOfficeDao extends BaseDao<UserOffice> {
	
	public List<Map<String,String>>  getUserDeptList(String userId)
	{
		String hql="from UserOffice where user.id='"+userId+"' and delFlag="+UserOffice.DEL_FLAG_NORMAL;
		List<UserOffice> userOfficeList = this.find(hql);
		
		List<Map<String,String>> mapList= new ArrayList<Map<String,String>>();
				
		for(UserOffice userOffice:userOfficeList)
		{
			Office office=userOffice.getDept();
			Office company=userOffice.getCompany();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("officeId", office.getId());
			map.put("officeName", office.getName());
			map.put("companyId", company.getId());
			map.put("companyName", company.getName());
			
			mapList.add(map);
		}
		
		return mapList;
	}
}
