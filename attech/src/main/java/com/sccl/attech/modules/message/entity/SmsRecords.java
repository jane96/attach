/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.sys.entity.Office;
import com.sccl.attech.modules.sys.entity.User;

/**
 * 短信记录表（sms_records）Entity
 * @author lxb
 * @version 2015-05-14
 */
@Entity
@Table(name = "message_smsRecords")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsRecords extends IdEntity<SmsRecords> {
	
	private static final long serialVersionUID = 1L;
	private Office company; 		//公司编号
	private Office office; 		// 部门编号
	private Integer mobile; 		//手机号码 
	private User sender; 		//发信人 
	private String sendTime; 		// 发送时间
	private String priceType; 		// 资费类型（0：免费；1：收费）
	private String operator; 		// 运营商
	private String basedataType; 		// 短信分类(关联sys_basedata主键编号)
	private String content; 		// 短信内容
	private Integer sort; 		// 排序号
	
	private List<SmsReceiver> smsReceivers = Lists.newArrayList();// 一条短信内容	——	多个接收人 
	
	private String receiverNames;	//收信人
	private String receiverIds;
	private String sendName;
	private String sendId;

	public SmsRecords() {
		super();
	}

	public SmsRecords(String id){
		this();
		this.id = id;
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

	public Integer getMobile() {
		return mobile;
	}

	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sender_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	@ExcelField(title="上级部门", align=2, sort=30)
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getBasedataType() {
		return basedataType;
	}

	public void setBasedataType(String basedataType) {
		this.basedataType = basedataType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	@OneToMany(mappedBy = "smsRecords", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<SmsReceiver> getSmsReceivers() {
		return smsReceivers;
	}

	public void setSmsReceivers(List<SmsReceiver> smsReceivers) {
		this.smsReceivers = smsReceivers;
	}

	@Transient
	public String getReceiverNames() {
		return receiverNames;
	}

	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}

	@Transient
	public String getSendName() {
		if(StringUtils.isBlank(this.sendName)){
			if(sender!=null){
				return sender.getName();
			}
		}
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	@Transient
	public String getSendId() {
		if(StringUtils.isBlank(this.sendId)){
			if(sender!=null){
				return sender.getId();
			}
		}
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	@Transient
	public String getReceiverIds() {
		return receiverIds;
	}

	public void setReceiverIds(String receiverIds) {
		this.receiverIds = receiverIds;
	}
	
}


