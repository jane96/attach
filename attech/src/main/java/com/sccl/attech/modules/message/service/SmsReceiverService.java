/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.message.dao.SmsReceiverDao;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.message.entity.SmsRecords;
import com.sccl.attech.modules.message.vo.SmsReceiverVo;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 短信收信人员表（sms_receiver）Service
 * @author lxb
 * @version 2015-05-14
 */
@Component
@Transactional(readOnly = true)
public class SmsReceiverService extends BaseService {

	@Autowired
	private SmsReceiverDao smsReceiverDao;
	
	public SmsReceiver get(String id) {
		return smsReceiverDao.get(id);
	}
	
	public Page<SmsReceiver> find(Page<SmsReceiver> page, SmsReceiver smsReceiver) {
		DetachedCriteria dc = smsReceiverDao.createDetachedCriteria();
		String recordsId = smsReceiver.getSmsRecords().getId();
		if (StringUtils.isNotEmpty(recordsId)){
			dc.add(Restrictions.eq("smsRecords.id", recordsId));
		}
		dc.createAlias("receiver", "receiver");
		if(StringUtils.isNotBlank(smsReceiver.getReceiverName())||StringUtils.isNotBlank(smsReceiver.getReceiverPhone())){
			dc.add(Restrictions.or(Restrictions.like("receiver.name", "%"+smsReceiver.getReceiverName()+"%"), Restrictions.like("receiver.phone", "%"+smsReceiver.getReceiverPhone()+"%")));
//			dc.add(Restrictions.like("receiver.name", "'%"+smsReceiver.getReceiverName()+"%'"));
		}
//		if(StringUtils.isNotBlank(smsReceiver.getReceiverPhone())){
//			dc.add(Restrictions.like("receiver.phone", "'%"+smsReceiver.getReceiverPhone()+"%'"));
//		}
		dc.add(Restrictions.eq(SmsReceiver.FIELD_DEL_FLAG, SmsReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return smsReceiverDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SmsReceiver smsReceiver) {
		smsReceiverDao.save(smsReceiver);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		smsReceiverDao.deleteById(id);
	}
	

	@Transactional(readOnly = false)
	public void save(SmsRecords records,String status) {
		String idStr = records.getReceiverIds();
		String ids[] = idStr.trim().split(",");
		List<SmsReceiver> list = new ArrayList<SmsReceiver>();
		
		for (String id : ids) {
			SmsReceiver receiver = new SmsReceiver();
			User user = UserUtils.getUserById(id);
			if(user!=null){
				receiver.setReceiver(user);//设置收信人
				receiver.setStatus(status);	//设置短信状态
				receiver.setCompany(user.getCompany());
				receiver.setOffice(user.getOffice());
				receiver.setSmsRecords(records);
				
				list.add(receiver);
			}
		}
		smsReceiverDao.save(list);
	}
	
	/**
	 * 获取短信对话列表数据 (舍弃)
	 * @param page 
	 * @param vo
	 * @return
	 */
	public Page<SmsReceiver> getListData(Page<SmsReceiver> page, SmsReceiver smsReceiver) {
		DetachedCriteria dc = smsReceiverDao.createDetachedCriteria();
		//dc.createAlias("receiver", "receiver"); //收信
		dc.createAlias("smsRecords", "smsRecords");//发信
		
//		dc.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		dc.setProjection(Projections.groupProperty("smsRecords.sender.id"));
		dc.add(Restrictions.eq("smsRecords.basedataType", "0"));
		dc.add(Restrictions.eq(SmsReceiver.FIELD_DEL_FLAG, SmsReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("smsRecords.sendTime"));
		
		return smsReceiverDao.find(page, dc);
	}

	public List<SmsReceiverVo> getDialogData(SmsReceiverVo vo) {
		String sendId = vo.getSendId();
		String receiverId = vo.getReceiverId();
		DetachedCriteria dc = smsReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver"); //收信
		dc.createAlias("smsRecords", "smsRecords");//发信
		
		if( StringUtils.isNotBlank(sendId)|| StringUtils.isNotBlank(receiverId) ) 
		{
			dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("receiver.id", receiverId),Restrictions.eq("smsRecords.sender.id", sendId)), Restrictions.and(Restrictions.eq("receiver.id", sendId),Restrictions.eq("smsRecords.sender.id", receiverId))));
		}
		dc.add(Restrictions.or(Restrictions.eq("smsRecords.basedataType", "0"), Restrictions.eq("smsRecords.basedataType", "2")));
		dc.add(Restrictions.eq(SmsReceiver.FIELD_DEL_FLAG, SmsReceiver.DEL_FLAG_NORMAL));
//		dc.add(Restrictions.ne("smsRecords.basedataType", "1"));
		dc.addOrder(Order.asc("smsRecords.sendTime"));
		List<SmsReceiver> list = smsReceiverDao.find(dc);
		List<SmsReceiverVo> list2 = new ArrayList<SmsReceiverVo>();
		for (SmsReceiver item : list) {
			SmsReceiverVo temp =  new SmsReceiverVo();
			temp.setId(item.getId());
			temp.setSendTime(item.getSmsRecords().getSendTime());
			temp.setBasedataType(item.getSmsRecords().getBasedataType());
			temp.setContent(item.getSmsRecords().getContent());
			temp.setSendId(item.getSmsRecords().getSendId());
			temp.setSendName(item.getSmsRecords().getSendName());
			temp.setReceiverId(item.getReceiver().getId());
			temp.setReceiverName(item.getReceiver().getName());
			temp.setStatus(item.getStatus());
			
			list2.add(temp);
		}
		return list2;
	}
	
	
	
}
