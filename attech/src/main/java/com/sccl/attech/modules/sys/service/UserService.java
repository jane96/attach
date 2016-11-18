package com.sccl.attech.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.entity.User;

@Service
@Transactional(readOnly = true)
public class UserService  extends BaseService  {
	@Autowired
	private UserDao userDao;
	
	public User get(String id)
	{
		return this.userDao.get(id);
	}
}
