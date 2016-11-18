/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;

/**
 * 短信收信人员表（sms_receiver）Entity
 * @author lxb
 * @version 2015-05-14
 */
@Entity
@Table(name = "message_smsReceiver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsReceiver extends IdEntity<SmsReceiver> {
	
	private static final long serialVersionUID = 1L;
	private SmsRecords smsRecords; 	// 短信记录编号
	private Office company;	//公司编号
	private Office office;	//部门编号
	private User receiver;	//收信人
	private String status;	//0成功 1失败
	
	private String receiverName; //收信人姓名 
	private String receiverPhone; //收信人电话
	private String recordsId; 	//短信记录Id;
	
	public static final String SMS_STATUS_SUCCESS = "0";
	public static final String SMS_STATUS_FAILE = "1";
	
	public SmsReceiver() {
		super();
	}

	public SmsReceiver(String id){
		this();
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sms_records_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	@ExcelField(title="发信", align=2, sort=30)
	public SmsRecords getSmsRecords() {
		return smsRecords;
	}

	public void setSmsRecords(SmsRecords smsRecords) {
		this.smsRecords = smsRecords;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	@ExcelField(title="归属公司", align=2, sort=30)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	@ExcelField(title="归属部门", align=2, sort=30)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="receiver_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	@ExcelField(title="收信人", align=2, sort=30)
	public User getReceiver() {
		return receiver;
	}
	
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * 0未读 1已读
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public String getReceiverName() {
		if(StringUtils.isBlank(this.receiverName)){
			if(receiver!=null){
				return receiver.getName();
			}
		}
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@Transient
	public String getReceiverPhone() {
		if(StringUtils.isBlank(this.receiverPhone)){
			if(receiver!=null){
				return receiver.getMobile();
			}
		}
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	@Transient
	public String getRecordsId() {
		if(StringUtils.isBlank(this.recordsId)){
			if(smsRecords!=null){
				return smsRecords.getId();
			}
		}
		return recordsId;
	}

	public void setRecordsId(String recordsId) {
		this.recordsId = recordsId;
	}
	
	
	
	 
}


