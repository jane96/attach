/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.sys.entity.Office;

/**
 * 短信收费表（sms_tariff）Entity
 * @author lxb
 * @version 2015-05-14
 */
@Entity
@Table(name = "message_smsTariff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsTariff extends IdEntity<SmsTariff> {
	
	private static final long serialVersionUID = 1L;
	private Office company;	//公司编号
	private Integer smsNum;	//短信总条数
	private Integer sentNum;//已发送条数
	private Integer unsentNum;//剩余短信条数

	public SmsTariff() {
		super();
	}

	public SmsTariff(String id){
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

	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Integer getSentNum() {
		return sentNum;
	}

	public void setSentNum(Integer sentNum) {
		this.sentNum = sentNum;
	}

	public Integer getUnsentNum() {
		return unsentNum;
	}

	public void setUnsentNum(Integer unsentNum) {
		this.unsentNum = unsentNum;
	}
	 
	
}


