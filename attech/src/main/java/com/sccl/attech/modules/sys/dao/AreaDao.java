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
import com.sccl.attech.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author sccl
 * @version 2013-8-23
 */
@Repository
public class AreaDao extends BaseDao<Area> {
	
	public List<Area> findByParentIdsLike(String parentIds){
		return find("from Area where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Area> findAllList(){
		return find("from Area where delFlag=:p1 order by code", new Parameter(Area.DEL_FLAG_NORMAL));
	}
	
	public List<Area> findAllChild(Long parentId, String likeParentIds){
		return find("from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code", 
				new Parameter(Area.DEL_FLAG_NORMAL, parentId, likeParentIds));
	}
	
	public List<Area> findChildes(String parentId, String type){
		return find("from Area where delFlag=:p1 and (parent.id=:p2 or type=:p3) order by code", 
				new Parameter(Area.DEL_FLAG_NORMAL, parentId, type));
	}
	
	@SuppressWarnings("unchecked")
	public Area findByOutId(String type,String outId){
		String hql = "from Area where type=? and outId=?";;
		List<Area> areas = getSession().createQuery(hql).setParameter(0, type).setParameter(1, outId).list();
		if(areas != null && areas.size() > 0){
			return areas.get(0);
		}
		return null;
	}
}
