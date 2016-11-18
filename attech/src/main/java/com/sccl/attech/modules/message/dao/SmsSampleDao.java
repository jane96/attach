/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.dao;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.message.entity.SmsSample;

/**
 * 短信样例表（sms_sample）DAO接口
 * @author lxb
 * @version 2015-05-14
 */
@Repository
public class SmsSampleDao extends BaseDao<SmsSample> {
	
}
