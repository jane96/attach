/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.modules.message.dao.NoticeReceiverDao;
import com.sccl.attech.modules.message.entity.NoticeReceiver;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.websocket.WebSocketUtil;

/**
 * 通知收信人员Service
 * @author denghc
 * @version 2015-05-13
 */
@Component
@Transactional(readOnly = true)
public class NoticeReceiverService extends BaseService {

	@Autowired
	private NoticeReceiverDao noticeReceiverDao;
	
	public NoticeReceiver get(String id) {
		return noticeReceiverDao.get(id);
	}
	
	public Page<NoticeReceiver> find(Page<NoticeReceiver> page, NoticeReceiver noticeReceiver) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(noticeReceiver.getState())){
			dc.add(Restrictions.like("name", "%"+noticeReceiver.getState()+"%"));
		}
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return noticeReceiverDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(NoticeReceiver noticeReceiver) {
		noticeReceiverDao.save(noticeReceiver);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		noticeReceiverDao.deleteById(id);
	}
	
	/**
	 * 获取通知记录下的收信人
	 * @param recordId
	 * @return
	 */
	public List<NoticeReceiver> findByRecordId(String recordId){
		List<NoticeReceiver> nrvList = noticeReceiverDao.findByRecordId(recordId);
		return nrvList;
	}

	/**
	 * 根据记录的id分页查询收信人数据
	 * @param page
	 * @param noticeReceiver
	 * @param recordId
	 * @return
	 */
	public Page<NoticeReceiver> findByRecordId(Page<NoticeReceiver> page,
			NoticeReceiver noticeReceiver, String recordId) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("noticeRecords", "noticeRecords");
		if (StringUtils.isNotEmpty(recordId)){
			dc.add(Restrictions.eq("noticeRecords.id",recordId));
		}
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return noticeReceiverDao.find(page, dc);
	}
	
	/**
	 * 发现所有未读通知 分页
	 * @param userId
	 * @param nowTime
	 */
	public Page<NoticeReceiver> findUnreadNotice(Page<NoticeReceiver> page,String userId,String state) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		if (StringUtils.isNotEmpty(state)){
			dc.add(Restrictions.eq("state",state));//通知是否已读取状态
		}
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return noticeReceiverDao.find(page, dc);
	}
	
	/**
	 * 发现所有未读通知 未分页
	 * @param userId
	 * @param nowTime
	 */
	public List<NoticeReceiver> findNotice(String userId,String state) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		if (StringUtils.isNotEmpty(state)){
			dc.add(Restrictions.eq("state",state));//是否已读
		}
		dc.addOrder(Order.desc("id"));
		return noticeReceiverDao.find(dc);
	}
	
	/**
	 * 发现是否存在新通知（在线状态收到的通知）
	 * @param userId
	 * @param loginTime
	 * 返回数据 新消息的大小
	 */
	public String findIsNotice(String userId, Date loginTime) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		dc.add(Restrictions.eq("state","0"));//状态为未读
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		List<NoticeReceiver> list = noticeReceiverDao.find(dc);
		String size = list.size()+"";
		return size;
	}
	
	/**
	 * 获取所有新通知（在线状态收到的未读通知或已读通知）
	 * @param userId
	 * @param loginTime
	 * 返回数据 新消息的大小
	 */
	public List<NoticeReceiver> findNewNotice(String userId, Date loginTime,String state) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		if (loginTime!=null){
			dc.add(Restrictions.ge("createDate",loginTime));
		}
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("state",state));//通知是否已读取状态
		}
		dc.addOrder(Order.desc("id"));
		return noticeReceiverDao.find(dc);
	}
	
	
	/**
	 * 获取所有通知（在线状态收到的未读通知或已读通知）
	 * @param userId
	 * @param loginTime
	 * 返回数据 
	 */
	public List<NoticeReceiver> findAllNotice(String userId) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return noticeReceiverDao.find(dc);
	}
	
	
	/**
	 *  获取对应人员最新的前五条通知
	 * @param userId
	 * @param loginTime
	 * 返回数据 
	 */
	public List<NoticeReceiver> findSomeNotice(String userId) {
		DetachedCriteria dc = noticeReceiverDao.createDetachedCriteria();
		dc.createAlias("receiver", "receiver");
		if (StringUtils.isNotEmpty(userId)){
			dc.add(Restrictions.eq("receiver.id",userId));
		}
		
		dc.add(Restrictions.eq("state",NoticeReceiver.NOTICE_STATE_NOTREAD));//通知是否已读取状态（0未读，1已读）
		Page<NoticeReceiver> page = new Page<NoticeReceiver>(1, 5);
		dc.add(Restrictions.eq(NoticeReceiver.FIELD_DEL_FLAG, NoticeReceiver.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		Page<NoticeReceiver> find = noticeReceiverDao.find(page,dc);
		List<NoticeReceiver> list = find.getList();
		return list;
	}
	
	/**
	 * 根据id改变通知的已读状态
	 * @param state
	 * @param receiverId
	 */
	public void changeNoticeState(String state, String receiverId) {
		String hql = "update NoticeReceiver nr set nr.state="+state+" where nr.id= '"+receiverId+"'";
		//Parameter parameter = new Parameter(state,receiverId);
		noticeReceiverDao.update(hql);
		User user = UserUtils.getUser();
		Map<String,String> map = new HashMap<String, String>();
		map.put("state", state);
		map.put("receiverId", receiverId);
		//调用webSocket给在线用户发送消息
		WebSocketUtil.sendMessageToUser(user.getId()+"_notice",new ResultData(true, "notice", map).Json());
	}
	
}
