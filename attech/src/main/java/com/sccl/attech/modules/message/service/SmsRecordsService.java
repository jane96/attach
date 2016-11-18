/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.sccl.attech.modules.message.dao.SmsRecordsDao;
import com.sccl.attech.modules.message.entity.SmsReceiver;
import com.sccl.attech.modules.message.entity.SmsRecords;
import com.sccl.attech.modules.sms.response.ResultInfo;
import com.sccl.attech.modules.sms.service.SmsService;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

/**
 * 短信记录表（sms_records）Service
 * @author lxb
 * @version 2015-05-14
 */
@Component
@Transactional(readOnly = true)
public class SmsRecordsService extends BaseService {

	@Autowired
	private SmsRecordsDao smsRecordsDao;
	@Autowired
	private SmsReceiverService receiverService;
	
	
	public SmsRecords get(String id) {
		return smsRecordsDao.get(id);
	}
	/**
	 * 查询短信发送记录，用于导出数据
	 * @param page
	 * @param smsRecords
	 * @return
	 */
	public List<SmsRecords> getList(SmsRecords smsRecords){
//		DetachedCriteria dc = smsRecordsDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(smsRecords.getBasedataType())){
//			dc.add(Restrictions.eq("basedataType", smsRecords.getBasedataType()));
//		}
//		if(StringUtils.isNotEmpty(smsRecords.getContent())){
//			dc.add(Restrictions.like("content", "%"+smsRecords.getContent()+"%"));
//		}
//		dc.add(Restrictions.eq(SmsRecords.FIELD_DEL_FLAG, SmsRecords.DEL_FLAG_NORMAL));
//		
//		List<SmsRecords> list=smsRecordsDao.find(dc);
//		for (SmsRecords item : list) {
//			String receiverNames = "";
//			String receiverIds = "";
//			List<SmsReceiver> list2 = item.getSmsReceivers();
//			for (SmsReceiver smsReceiver : list2) {
//				if(smsReceiver!=null&&smsReceiver.getReceiver()!=null){
//					receiverNames = receiverNames + smsReceiver.getReceiver().getName()+",";
//					receiverIds = receiverIds + smsReceiver.getReceiver().getId()+",";
//				}else{
//					receiverNames = "";
//					receiverIds ="";
//				}
//			}
//			if(receiverNames.length()>0){
//				receiverNames = receiverNames.substring(0, receiverNames.length()-1);
//				receiverIds = receiverIds.substring(0, receiverIds.length()-1);
//			}
//			
//			item.setReceiverNames(receiverNames);
//			item.setReceiverIds(receiverIds);
//		}
		Page<SmsRecords> page = new Page<SmsRecords>();
		page.setPageSize(-1);
		return find(page,smsRecords).getList();
	}
	public Page<SmsRecords> find(Page<SmsRecords> page, SmsRecords smsRecords) {
		User currentUser = UserUtils.getUser();
		DetachedCriteria dc = smsRecordsDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(smsRecords.getBasedataType())){
			dc.add(Restrictions.eq("basedataType", smsRecords.getBasedataType()));
		}
		if(StringUtils.isNotEmpty(smsRecords.getContent())){
			dc.add(Restrictions.like("content", "%"+smsRecords.getContent()+"%"));
		}
		dc.add(dataScopeFilter(currentUser, "office", "createBy"));//用户根据权限查看对应的短信发送记录
		//dc.createAlias("createBy","createBy");
		//dc.add(Restrictions.eq("createBy.id",currentUser.getId()));//用户只能查看自己的短信发送记录
		dc.add(Restrictions.eq(SmsRecords.FIELD_DEL_FLAG, SmsRecords.DEL_FLAG_NORMAL));
		Page<SmsRecords> pageData =smsRecordsDao.find(page, dc);
		
		List<SmsRecords> list = pageData.getList();
		for (SmsRecords item : list) {
			String receiverNames = "";
			String receiverIds = "";
			List<SmsReceiver> list2 = item.getSmsReceivers();
			for (SmsReceiver smsReceiver : list2) {
				if(smsReceiver!=null&&smsReceiver.getReceiver()!=null){
					receiverNames = receiverNames + smsReceiver.getReceiver().getName()+",";
					receiverIds = receiverIds + smsReceiver.getReceiver().getId()+",";
				}else{
					receiverNames = "";
					receiverIds ="";
				}
			}
			if(receiverNames.length()>0){
				receiverNames = receiverNames.substring(0, receiverNames.length()-1);
				receiverIds = receiverIds.substring(0, receiverIds.length()-1);
			}
			
			item.setReceiverNames(receiverNames);
			item.setReceiverIds(receiverIds);
		}
		
		pageData.setList(list);
		return pageData;
	}
	
	@Transactional(readOnly = false)
	public String save(SmsRecords smsRecords) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		try {
			User user = UserUtils.getUser();
			SmsRecords records = new SmsRecords();
			records.setCompany(user.getCompany()); //设置发信人公司
			records.setOffice(user.getOffice()); //设置发信人部门
			records.setContent(smsRecords.getContent()); //设置发送内容
			records.setSender(user); //发送人
			records.setBasedataType(smsRecords.getBasedataType()); //设置短信类型
			records.setSendTime(DateUtils.getDateTime());
			
			String mobiles = UserUtils.getPhoneByUserId(smsRecords.getReceiverIds());
			
			SmsService service =new SmsService();
			resultInfo = service.sendSms(mobiles, smsRecords.getContent());
			
			smsRecordsDao.save(records);
			
			records.setReceiverIds(smsRecords.getReceiverIds());
			receiverService.save(records,resultInfo.getResult());
			return resultInfo.getResult();
		} catch (Exception e) {
			throw new Exception("插入短信记录异常！");
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		smsRecordsDao.deleteById(id);
	}
	// 获取资费类型（0：免费；1：收费）
	public String getPriceType(String priceType){
		String priceTypeValue="";
		if(priceType != null){
			if(priceType.equals("0")){
				priceTypeValue="免费";
			}
			else if(priceType.equals("1")){
				priceTypeValue="收费";
			}
		}
		return priceTypeValue;
	}
	
	/**
	 * 导出短信发送记录数据
	 * @param attendanceRecord
	 * @param request
	 * @param response
	 * @return
	 */	
	public String exportFile(SmsRecords smsRecords, HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/templates");
		String n = DateUtils.getDate("yyyyMMddHHmmss")+"smsRecords.xls";
		File f = new File(path+"/"+n);
		ExcelWriter e = new ExcelWriter();
		try {
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "序号 ");
			e.setTitleCell(1, "短信内容");
			e.setTitleCell(2, "发信人");			
			e.setTitleCell(3, "收信人");
			e.setTitleCell(4, "发送时间");
			/*e.setTitleCell(5, "手机号码");
			e.setTitleCell(6, "资费类型");
			e.setTitleCell(7, "运营商");
			e.setTitleCell(8, "排序号");*/
			List<SmsRecords> list=getList(smsRecords);
			if(list!=null&&list.size()>0){
				for(int i=1;i<=list.size();i++){
					e.createRow(i);
					e.setCell(0, i);
					if(list.get(i-1).getContent()==null){
						e.setCell(1, " ");
					}else{
						e.setCell(1, list.get(i-1).getContent());
					}
					if(list.get(i-1).getSendName()==null){
						e.setCell(2, " ");
					}else{
						e.setCell(2, list.get(i-1).getSendName());
					}
					if(list.get(i-1).getReceiverNames()==null||list.get(i-1).getReceiverNames()==""){
						e.setCell(3, " ");
					}else{
						e.setCell(3, list.get(i-1).getReceiverNames());
					}
					if(list.get(i-1).getSendTime()==null){
						e.setCell(4, " ");
					}else{
						e.setCell(4, list.get(i-1).getSendTime());
					}
					/*if(list.get(i-1).getMobile()==null){
						e.setCell(5, " ");
					}else{
						e.setCell(5, list.get(i-1).getMobile());
					}
					if(list.get(i-1).getPriceType()==null){
						e.setCell(6, " ");
					}else{
						String priceType=list.get(i-1).getPriceType();
						String priceTypeValue=getPriceType(priceType);
						e.setCell(6, priceTypeValue);
					}
					if(list.get(i-1).getOperator()==null){
						e.setCell(7, " ");
					}else{
						e.setCell(7, list.get(i-1).getOperator());
					}
					if(list.get(i-1).getSort()==null){
						e.setCell(8, " ");
					}else{
						e.setCell(8, list.get(i-1).getSort());
					}*/
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
