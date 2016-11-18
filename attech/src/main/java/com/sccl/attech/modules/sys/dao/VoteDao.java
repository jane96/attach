package com.sccl.attech.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.Vote;
@Repository
public class VoteDao extends BaseDao<Vote> {
	/*public List<Vote> findAllList(){
		return find("from Vote", new Parameter(Vote.DEL_FLAG_NORMAL));
	}*/
}
