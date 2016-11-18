 /**
 * @(#)${ClassName}.java  ${classVersion}  ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ${packageName}.${moduleName}.entity${subModuleName};

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.sccl.attech.common.persistence.IdEntity;

/**
 * The ${functionName}Entity
 * 
 * @author ${classAuthor}
 * @version ${classVersion}, ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * @see ${packageName}.${moduleName}.web${subModuleName}
 * @since JDK1.7
 */
@Entity
@Table(name = "${tableName}")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ${ClassName} extends IdEntity<${ClassName}> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	<#list fields as field>
	/** The ${field.remark}. */
	private String ${field.name};
	</#list>
	
	/**
	 * Instantiates a new app model.
	 */
	public ${ClassName}() {
		super();
	}
	
	/**
	 * Instantiates a new app model.
	 * 
	 * @param id the 主键
	 */
	public ${ClassName}(String id){
		this();
		this.id = id;
	}
	<#list fields as field>
	
	/**
	 * Gets the ${field.remark}.
	 * 
	 * @return the ${field.name}
	 */
	@Length(min=1, max=200)
	public String get${field.name?cap_first}() {
		return ${field.name};
	}
	
	/**
	 * Sets the ${field.remark}.
	 * 
	 * @param ${field.name} the new ${field.remark}
	 */
	public void set${field.name?cap_first}(String ${field.name}) {
		this.${field.name} = ${field.name};
	}
	</#list>
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				<#list fields as field>
				append("${field.name}", this.${field.name}).
				</#list>
				toString();
	}
}


