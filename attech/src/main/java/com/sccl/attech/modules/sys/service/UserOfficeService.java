package com.sccl.attech.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.modules.sys.dao.UserOfficeDao;
import com.sccl.attech.modules.sys.entity.UserOffice;

@Service
@Transactional(readOnly = true)
public class UserOfficeService extends BaseService {

	@Autowired
	private UserOfficeDao userOfficeDao;
	
	public void save(UserOffice userOffice)
	{
		this.userOfficeDao.save(userOffice);
	}
	
	public List<Map<String,String>>  getUserDeptList(String userId)
	{
		return this.userOfficeDao.getUserDeptList(userId);
	}
}
