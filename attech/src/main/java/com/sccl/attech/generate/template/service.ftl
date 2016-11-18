/**
 * @(#)${ClassName}Service.java  ${classVersion}  ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ${packageName}.${moduleName}.service${subModuleName};

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sccl.attech.common.persistence.Page;
import com.sccl.attech.common.service.BaseService;
import com.sccl.attech.common.utils.StringUtils;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.dao${subModuleName}.${ClassName}Dao;
 
/**
 * The ${functionName}Service
 * 
 * @author ${classAuthor}
 * @version ${classVersion},${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * @see ${packageName}.${moduleName}.web${subModuleName}
 * @since JDK1.7
 */
@Component
@Transactional(readOnly = true)
public class ${ClassName}Service extends BaseService {

	@Autowired
	private ${ClassName}Dao ${className}Dao;
	
	public ${ClassName} get(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(String=" + id + ") - 开始");
		}
		${ClassName} ${className}=${className}Dao.get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("get(String=" + id + ") - 结束 - 返回值=" + ${className});
		}
		return ${className};
	}
	
	public Page<${ClassName}> find(Page<${ClassName}> page, ${ClassName} ${className}) {
		if (logger.isDebugEnabled()) {
			logger.debug("find(Page<${ClassName}>=" + page + ", ${ClassName}=" + ${className} + ") - 开始");
		}
		
		DetachedCriteria dc = ${className}Dao.createDetachedCriteria();
		<#list fields as field>
		if (StringUtils.isNotEmpty(${className}.get${field.name?cap_first}())){
			dc.add(Restrictions.like("${field.name}", "%"+${className}.get${field.name?cap_first}()+"%"));
		}
		</#list>
		dc.add(Restrictions.eq(${ClassName}.FIELD_DEL_FLAG, ${ClassName}.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("updateDate"));
		Page<${ClassName}> returnPage =${className}Dao.find(page, dc);
		
		if (logger.isDebugEnabled()) {
			logger.debug("find(Page<${ClassName}>=" + page + ", ${ClassName}=" + ${className} + ") - 结束 - 返回值=" + page);
		}
		return returnPage;
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(${ClassName}=" + ${className} + ") - 开始");
		}
		${className}Dao.save(${className});
		if (logger.isDebugEnabled()) {
			logger.debug("save(${ClassName}=" + ${className} + ") - 结束");
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 开始");
		}
		${className}Dao.deleteById(id);
		
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String=" + id + ") - 结束");
		}
	}
	
}
