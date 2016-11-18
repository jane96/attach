/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.modules.message.dao.NoticeReceiverDao;
import com.sccl.attech.modules.message.dao.NoticeRecordsDao;
import com.sccl.attech.modules.message.entity.NoticeReceiver;
import com.sccl.attech.modules.message.entity.NoticeRecords;
import com.sccl.attech.modules.message.vo.NoticeRecordsVo;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.service.SystemService;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.websocket.WebSocketUtil;

/**
 * 通知记录Service
 * @author sccl
 * @version 2015-05-13
 */
@Component
@Transactional(readOnly = true)
public class NoticeRecordsService extends BaseService {

	@Autowired
	private NoticeRecordsDao noticeRecordsDao;
	@Autowired
	private NoticeReceiverDao noticeReceiverDao;
	@Autowired
	private SystemService systemService;	
	@Autowired
	private NoticeReceiverService noticeReceiverService;
	

	public NoticeRecords get(String id) {
		return noticeRecordsDao.get(id);
	}
	public Page<NoticeRecords> find(Page<NoticeRecords> page, NoticeRecords noticeRecords) {
		User currentUser = UserUtils.getUser();
		DetachedCriteria dc = noticeRecordsDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(noticeRecords.getTitle())){
			dc.add(Restrictions.like("title", "%"+noticeRecords.getTitle()+"%"));
		}
		dc.add(dataScopeFilter(currentUser, "office", "createBy"));//用户根据权限查看对应的通知发送记录
		//dc.createAlias("createBy","createBy");
		//dc.add(Restrictions.eq("createBy.id",currentUser.getId()));//用户只能查看自己的通知发送记录
		dc.add(Restrictions.eq(NoticeRecords.FIELD_DEL_FLAG, NoticeRecords.DEL_FLAG_NORMAL));
		return noticeRecordsDao.find(page, dc);
	}	
	/**
	 * 用于导出数据的查询
	 * @param noticeRecords
	 * @return
	 */
	public List<NoticeRecordsVo> getList(NoticeRecords noticeRecords){
		
		Page<NoticeRecords> page = new Page<NoticeRecords>();
		page.setPageSize(-1);
		List<NoticeRecords> nrdList=find(page,noticeRecords).getList();		
        List<NoticeRecordsVo> nrvList = new ArrayList<NoticeRecordsVo>();
        
        for (NoticeRecords n : nrdList) {
        	NoticeRecordsVo nrv = new NoticeRecordsVo();
        	nrv.setId(n.getId());
        	nrv.setSendTime(n.getCreateDate());
        	nrv.setTitle(n.getTitle());
        	nrv.setContent(n.getContent());
        	nrv.setSenderName(n.getSender().getName());
        	nrv.setSenderId(n.getSender().getId());// receiverName,receiverId,state,acceptingState
        	//nrv.setReceiverName(receiverName);
        	List<NoticeReceiver> nrList = noticeReceiverService.findByRecordId(n.getId());
        	String reName = "";
        	String reId = "";
        	for (NoticeReceiver nr : nrList) {
        		if(nr!=null&&nr.getReceiver()!=null){
        			reName =reName+nr.getReceiver().getName()+",";
        			reId = nr.getReceiver().getId();
        		}
			}
            nrv.setReceiverName(reName);
            nrv.setReceiverId(reId);
            nrvList.add(nrv);
		}
        return nrvList;
        
	}
	@Transactional(readOnly = false)
	public void save(NoticeRecords noticeRecords,String reserverId) {
		User user = UserUtils.getUser();
		noticeRecords.setCompany(user.getCompany());
		noticeRecords.setOffice(user.getOffice());
		noticeRecords.setSender(user);
		noticeRecords.setSendTime(new Date());
		noticeRecordsDao.save(noticeRecords);
		
		String[] ids = null;
		if(reserverId.contains(",")){
			ids = reserverId.split(",");
		}else{
			ids = new String[1];
			ids[0]=reserverId;
		}
		for (String id : ids) {
			NoticeReceiver nr = new NoticeReceiver();
			User reserver = systemService.getUser(id);
			nr.setCompany(reserver.getCompany());
			nr.setOffice(reserver.getOffice());
			nr.setReceiver(reserver);
			nr.setNoticeRecords(noticeRecords);
			nr.setState("0");
			noticeReceiverDao.save(nr);
			//调用webSocket给在线用户发送消息
			WebSocketUtil.sendMessageToUser(id+"_notice",new ResultData(true, "notice", noticeRecords.getContent()).Json());
		}
		//调用webSocket给在线用户发送消息
		//WebSocketUtil.sendMessageToUser(UUID.randomUUID()+"_notice",new ResultData(true, "notice", noticeRecords).Json());
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		noticeRecordsDao.deleteById(id);
	}
	
	//导出文件
	public String exportFile(NoticeRecords noticeRecords , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"noticeRecords.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "通知标题");
			e.setTitleCell(2, "发送人");	
			e.setTitleCell(3, "接收人");
			e.setTitleCell(4, "发送时间");	
			List<NoticeRecordsVo> list=getList(noticeRecords);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					e.setCell(1, list.get(i-1).getTitle());
					if(list.get(i-1).getSenderName()==null){
						e.setCell(2, "  ");
					}else{
						e.setCell(2, list.get(i-1).getSenderName());
					}					
					if(list.get(i-1).getReceiverName()==null||list.get(i-1).getReceiverName()==""){
						e.setCell(3, "  ");
					}else{
						e.setCell(3, list.get(i-1).getReceiverName());
					}
					if(list.get(i-1).getSendTime()==null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getSendTime());
					}										
				}
			}
			try {
				e.export();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		return n;
	}
	
	/**
	 * 根据userId获取用户
	 * @param outId 省管项目经理id
	 * @return
	 */
	public User findByOutId(String outId){
		return systemService.findByOutId(outId);
	}
	
	/**
	 * 发送项目通知
	 * @param noticeRecords
	 * @param reserverId
	 */
	@Transactional(readOnly = false)
	public void saveProjectNotice(NoticeRecords noticeRecords,String reserverId,String projectId) {
		
		noticeRecordsDao.save(noticeRecords);
		
		String[] ids = null;
		if(reserverId.contains(",")){
			ids = reserverId.split(",");
		}else{
			ids = new String[1];
			ids[0]=reserverId;
		}
		for (String id : ids) {
			NoticeReceiver nr = new NoticeReceiver();
			User reserver = systemService.getUser(id);
			nr.setCompany(reserver.getCompany());
			nr.setOffice(reserver.getOffice());
			nr.setReceiver(reserver);
			nr.setNoticeRecords(noticeRecords);
			nr.setState("0");
			nr.setProjectId(projectId); 
			noticeReceiverDao.save(nr);
			//调用webSocket给在线用户发送消息
			WebSocketUtil.sendMessageToUser(id+"_notice",new ResultData(true, "notice", noticeRecords.getContent()).Json());
		}
	}
	
	
	/**
	 * 项目通知的删除
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteByPro(String receiverId,String projectId) {
		noticeReceiverDao.deleteByPro(receiverId,projectId);
	}
	
}
