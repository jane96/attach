/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import com.sccl.attech.common.persistence.Parameter;
import com.sccl.attech.modules.message.entity.NoticeReceiver;

/**
 * 通知收信人员DAO接口
 * @author denghc
 * @version 2015-05-13
 */
@Repository
public class NoticeReceiverDao extends BaseDao<NoticeReceiver> {

	/**
	 * 获取通知记录下的收信人
	 * @param recordId
	 * @return
	 */
	public List<NoticeReceiver> findByRecordId(String recordId) {
		String hql = "select nrv from NoticeReceiver nrv join nrv.noticeRecords as nrd where nrd.id='"+recordId+"'";
		return find(hql);
	}
	
	/**
	 * 删除项目通知信息（物理删除）；
	 * @param receiverId
	 * @param projectId
	 */
	public void deleteByPro(String receiverId,String projectId){
		String hql = "delete from NoticeReceiver where receiver.id='"+receiverId+"' and projectId='"+projectId+"'";
		getSession().createQuery(hql).executeUpdate();
	}
}
