/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.DateUtils;
import com.sccl.attech.common.utils.EncodedUtil;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.ExcelWriter;
import com.sccl.attech.common.vo.ResultData;
import com.sccl.attech.modules.message.dao.AlarmRecordsDao;
import com.sccl.attech.modules.message.entity.AlarmRecords;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;
import com.sccl.attech.modules.websocket.WebSocketUtil;

/**
 * 告警记录Service
 * @author denghc
 * @version 2015-05-18
 */
@Component
@Transactional(readOnly = true)
public class AlarmRecordsService extends BaseService {

	@Autowired
	private AlarmRecordsDao alarmRecordsDao;
	
	public AlarmRecords get(String id) {
		return alarmRecordsDao.get(id);
	}
	
	public Page<AlarmRecords> find(Page<AlarmRecords> page, AlarmRecords alarmRecords) {
		DetachedCriteria dc = alarmRecordsDao.createDetachedCriteria();
		dc.createAlias("user", "user");
		if(alarmRecords.getUser()!=null&&StringUtils.isNotEmpty(alarmRecords.getUser().getName())){
			String searchLable = EncodedUtil.decodeValue(alarmRecords.getUser().getName());
			dc.add(Restrictions.like("user.name","%"+searchLable+"%"));
		}
		User currentUser = UserUtils.getUser();
		dc.createAlias("office","office");
		dc.add(dataScopeFilter(currentUser, "office", "createBy"));//用户根据权限查看对应的告警发送记录
		if(alarmRecords.getLocation_date()!=null){
			Date startTime = DateUtils.getDateStart(alarmRecords.getLocation_date());
			Date endTime = DateUtils.getDateEnd(alarmRecords.getLocation_date());
			dc.add(Restrictions.ge("location_date",startTime));
			dc.add(Restrictions.le("location_date",endTime));
		}
		return alarmRecordsDao.find(page, dc);
	}
	/**
	 * 用于导出数据的查询
	 * @param alarmRecords
	 * @return
	 */
	public List<AlarmRecords> getList(AlarmRecords alarmRecords){
		Page<AlarmRecords> page = new Page<AlarmRecords>();
		return find(page,alarmRecords).getList();
	}
	
	@Transactional(readOnly = false)
	public void save(AlarmRecords alarmRecords) {
		if(null==alarmRecords.getUser())return;
		String userId=alarmRecords.getUser().getId();
		User user=UserUtils.getUserById(userId);
		alarmRecords.initByUser(user);
		alarmRecords.setType(AlarmRecords.ALARM_RECORD_TYPE);
		alarmRecords.setContent("不在围栏范围内");
		alarmRecordsDao.save(alarmRecords);
		WebSocketUtil.sendMessageToUser(userId+"_alarm",new ResultData(true, "alarm", alarmRecords.getContent()).Json());
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		alarmRecordsDao.deleteById(id);
	}
	
	public Page<AlarmRecords> find(Page<AlarmRecords> page, Office office) {
		User user = UserUtils.getUser();
//		if (user.isAdmin()){
//			officeList = officeDao.findAllList();
//		}else{
//			officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
//		}
		
		DetachedCriteria dc = alarmRecordsDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(office.getName())){
//			dc.add(Restrictions.like("name", "%"+office.getName()+"%"));
//		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return alarmRecordsDao.find(page, dc);
	}

	/**
	 *  获取对应部门所有告警记录（）
	 * @param offficeId
	 * @return
	 */
	public List<AlarmRecords> findAllAlarm(String offficeId) {
		DetachedCriteria dc = alarmRecordsDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		User user = UserUtils.getUser();
		dc.createAlias("user","user");
		if(StringUtils.isNotEmpty(user.getId())){
			dc.add(Restrictions.eq("user.id",user.getId()));
		}
		if (StringUtils.isNotEmpty(offficeId)){
			dc.add(Restrictions.eq("office.id",offficeId));
		}
		dc.add(Restrictions.eq(AlarmRecords.FIELD_DEL_FLAG, AlarmRecords.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return alarmRecordsDao.find(dc);
	}
	
	/**
	 *  获取最新告警记录前五条 
	 * @param offficeId
	 * @return
	 */
	public List<AlarmRecords> findSomeAlarm(String offficeId) {
		DetachedCriteria dc = alarmRecordsDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("user","user");
		if(StringUtils.isNotEmpty(user.getId())){
			dc.add(Restrictions.eq("user.id",user.getId()));
		}
		dc.createAlias("office", "office");
		if (StringUtils.isNotEmpty(offficeId)){
			dc.add(Restrictions.eq("office.id",offficeId));
		}
		dc.add(Restrictions.eq("state",AlarmRecords.ALARM_STATE_NOTREAD));//状态为未读
		Page<AlarmRecords> page = new Page<AlarmRecords>(1, 5);
		dc.add(Restrictions.eq(AlarmRecords.FIELD_DEL_FLAG, AlarmRecords.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		Page<AlarmRecords> find = alarmRecordsDao.find(page,dc);
		List<AlarmRecords> list = find.getList();
		return list;
	}
	
	/**
	 * 根据id改变记录的已读状态
	 * @param state
	 * @param alarmId
	 */
	public void changeNoticeState(String state, String alarmId) {
		String hql = "update AlarmRecords nr set nr.state="+state+" where nr.id= '"+alarmId+"'";
		//Parameter parameter = new Parameter(state,receiverId);
		alarmRecordsDao.update(hql);
		Map<String,String> map = new HashMap<String, String>();
		map.put("state", state);
		map.put("receiverId", alarmId);
		User user=UserUtils.getUser();
		WebSocketUtil.sendMessageToUser(user.getId()+"_alarm",new ResultData(true, "alarm", map).Json());
	}

	/**
	 * 发现是否存在未读告警记录
	 * @param offficeId
	 * @return
	 */
	public String findIsNotice(String offficeId) {
		DetachedCriteria dc = alarmRecordsDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("user","user");
		if(StringUtils.isNotEmpty(user.getId())){
			dc.add(Restrictions.eq("user.id",user.getId()));
		}
		
		dc.createAlias("office", "office");
		if (StringUtils.isNotEmpty(offficeId)){
			dc.add(Restrictions.eq("office.id",offficeId));
		}
		dc.add(Restrictions.eq("state",AlarmRecords.ALARM_STATE_NOTREAD));//状态为未读
		dc.add(Restrictions.eq(AlarmRecords.FIELD_DEL_FLAG, AlarmRecords.DEL_FLAG_NORMAL));
		List<AlarmRecords> list = alarmRecordsDao.find(dc);
		String size = list.size()+"";
		return size;
	}
	/**
	 * 根据是否已读（0,1）获取字符串是否已读（0：未读，1：已读）
	 * @param state
	 * @return
	 */
	public String getAlarmState(String state){
		String stateValue="";
		if(state.equals("0")){
			stateValue="未读";
		}
		else if(state.equals("1")){
			stateValue="已读";
		}
		return stateValue;		
	}
	//导出文件
	public String exportFile(AlarmRecords alarmRecords , HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"alarm.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "外勤人员");
			e.setTitleCell(2, "告警类型");	
			e.setTitleCell(3, "告警内容");
			e.setTitleCell(4, "告警发生时间");
			e.setTitleCell(5, "告警发生地点");
			e.setTitleCell(6, "是否已读");
			List<AlarmRecords> list=getList(alarmRecords);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					if(list.get(i-1).getName() == null){
						e.setCell(1, "  ");
					}else{
						e.setCell(1, list.get(i-1).getName());	
					}
					if(list.get(i-1).getTypeName() == null){
						e.setCell(2, "  ");
					}else{
						e.setCell(2, list.get(i-1).getTypeName());
					}					
					if(list.get(i-1).getContent() == null){
						e.setCell(3, "  ");
					}else{
						e.setCell(3, list.get(i-1).getContent());
					}
					if(list.get(i-1).getLocation_date() == null){
						e.setCell(4, "  ");
					}else{
						e.setCell(4, list.get(i-1).getLocation_date());
					}
					if(list.get(i-1).getLocation_desc() == null){
						e.setCell(5, "  ");
					}else{
						e.setCell(5, list.get(i-1).getLocation_desc());
					}
					String state=list.get(i-1).getState();
					String stateValue=getAlarmState(state);
					e.setCell(6, stateValue);
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
}
