package com.sccl.attech.modules.sys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.sys.entity.Candidate;
import com.sccl.attech.modules.sys.entity.Dict;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.Vote;
@Repository
public class CandidateDao extends BaseDao<Candidate> {
	@Autowired
	private SessionFactory sessionFactory;
	public List<Candidate> findAllList(){
		return find("from Candidate where delFlag=:p1 ", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	public List<Candidate> find(){
		List<Candidate> list = new CandidateDao().find("from Candidate", new Parameter(Dict.DEL_FLAG_NORMAL));
		
		return list;
	}
	
	
}
