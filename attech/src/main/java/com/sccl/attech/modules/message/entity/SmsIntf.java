/**
 * There are <a href="https://github.com/sccl/attech">attech</a> code generation
 */
package com.sccl.attech.modules.message.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sccl.attech.common.persistence.IdEntity;
import com.sccl.attech.modules.sys.entity.Office;

/**
 * 短信接口表（sms_intf）Entity
 * @author lxb
 * @version 2015-05-14
 */
@Entity
@Table(name = "message_smsIntf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsIntf extends IdEntity<SmsIntf> {
	
	private static final long serialVersionUID = 1L;
	private Office company;	// 公司编号

	public SmsIntf() {
		super();
	}
	
	public SmsIntf(String id) {
		this();
		this.id = id;
	}

	@Transient
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	

	
}


