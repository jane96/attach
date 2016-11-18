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
import com.sccl.attech.modules.message.entity.SmsTariff;
import com.sccl.attech.modules.message.dao.SmsTariffDao;

/**
 * 短信收费表（sms_tariff）Service
 * @author lxb
 * @version 2015-05-14
 */
@Component
@Transactional(readOnly = true)
public class SmsTariffService extends BaseService {

	@Autowired
	private SmsTariffDao smsTariffDao;
	
	public SmsTariff get(String id) {
		return smsTariffDao.get(id);
	}
	
	public Page<SmsTariff> find(Page<SmsTariff> page, SmsTariff smsTariff) {
		DetachedCriteria dc = smsTariffDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(smsTariff.getName())){
//			dc.add(Restrictions.like("name", "%"+smsTariff.getName()+"%"));
//		}
		dc.add(Restrictions.eq(SmsTariff.FIELD_DEL_FLAG, SmsTariff.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return smsTariffDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SmsTariff smsTariff) {
		smsTariffDao.save(smsTariff);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		smsTariffDao.deleteById(id);
	}
	
}
