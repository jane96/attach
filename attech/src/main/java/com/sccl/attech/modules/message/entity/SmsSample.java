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
 * 短信样例表（sms_sample）Entity
 * @author lxb
 * @version 2015-05-14
 */
@Entity
@Table(name = "message_smsSample")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsSample extends IdEntity<SmsSample> {
	
	private static final long serialVersionUID = 1L;
	private Office company;
	private Office office;
	private String content;//短信模板内容

	public SmsSample() {
		super();
	}

	public SmsSample(String id){
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

	@Transient
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	 
	
}


