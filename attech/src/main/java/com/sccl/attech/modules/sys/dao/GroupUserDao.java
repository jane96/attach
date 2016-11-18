/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.Grouping;
import com.sccl.attech.modules.sys.entity.GroupingUsers;

/**
 * 群组DAO接口
 * @author 肖力
 * @version 2015-10-1
 */
@Repository
public class GroupUserDao extends BaseDao<GroupingUsers> {

	public List<GroupingUsers> findAllList(){
		return find("from GroupingUsers where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	public List<GroupingUsers> findDicType(String value){
		return find("from GroupingUsers where "+value+" order by sort");
	}

	public List<GroupingUsers> findTypeList(){
		return find("select type from GroupingUsers where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
}
