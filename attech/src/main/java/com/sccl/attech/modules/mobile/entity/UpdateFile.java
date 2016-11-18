 /**
 * @(#)UpdateFile.java  2015-07-14  2015-07-14 15:59:09 中国标准时间
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.mobile.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.sccl.attech.common.persistence.IdEntity;

/**
 * The App升级Entity
 * 
 * @author zzz
 * @version 2015-07-14, 2015-07-14 15:59:09 中国标准时间
 * @see com.sccl.attech.modules.mobile.web
 * @since JDK1.7
 */
@Entity
@Table(name = "mobile_updateFile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpdateFile extends IdEntity<UpdateFile> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The name. */
	private String name;
	/** The version. */
	private String version;
	/** The type. */
	private String type;
	/** The path. */
	private String path;
	/** The url. */
	private String url;
	
	/**
	 * Instantiates a new app model.
	 */
	public UpdateFile() {
		super();
	}
	
	/**
	 * Instantiates a new app model.
	 * 
	 * @param id the 主键
	 */
	public UpdateFile(String id){
		this();
		this.id = id;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Length(min=1, max=200)
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Length(min=1, max=200)
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param name the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	@Length(min=1, max=200)
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param name the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	@Length(min=1, max=200)
	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the path.
	 * 
	 * @param name the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	@Length(min=1, max=200)
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets the url.
	 * 
	 * @param name the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("name", this.name).
				append("version", this.version).
				append("type", this.type).
				append("path", this.path).
				append("url", this.url).
				toString();
	}
}


