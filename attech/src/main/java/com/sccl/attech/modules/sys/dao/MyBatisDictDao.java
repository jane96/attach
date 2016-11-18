/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.dao;

import java.util.List;

import com.sccl.attech.common.persistence.annotation.MyBatisDao;
import com.sccl.attech.modules.sys.entity.Dict;

/**
 * MyBatis字典DAO接口
 * @author sccl
 * @version 2013-8-23
 */
@MyBatisDao
public interface MyBatisDictDao {
	
    Dict get(String id);
    
    List<Dict> find(Dict dict);
    
}
