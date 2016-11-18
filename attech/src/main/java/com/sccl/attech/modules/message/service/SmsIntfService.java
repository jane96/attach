/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.message.entity.SmsIntf;
import com.sccl.attech.modules.message.dao.SmsIntfDao;

/**
 * 短信接口表（sms_intf）Service
 * @author lxb
 * @version 2015-05-14
 */
@Component
@Transactional(readOnly = true)
public class SmsIntfService extends BaseService {

	@Autowired
	private SmsIntfDao smsIntfDao;
	
	public SmsIntf get(String id) {
		return smsIntfDao.get(id);
	}
	
	public Page<SmsIntf> find(Page<SmsIntf> page, SmsIntf smsIntf) {
		DetachedCriteria dc = smsIntfDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(smsIntf.getName())){
//			dc.add(Restrictions.like("name", "%"+smsIntf.getName()+"%"));
//		}
		dc.add(Restrictions.eq(SmsIntf.FIELD_DEL_FLAG, SmsIntf.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return smsIntfDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SmsIntf smsIntf) {
		smsIntfDao.save(smsIntf);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		smsIntfDao.deleteById(id);
	}
	
}
