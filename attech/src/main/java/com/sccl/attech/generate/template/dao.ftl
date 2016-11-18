 /**
 * @(#)${ClassName}Dao.java  ${classVersion}  ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ${packageName}.${moduleName}.dao${subModuleName};

import org.springframework.stereotype.Repository;

import com.sccl.attech.common.persistence.BaseDao;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};

/**
 * The ${functionName}DAO接口
 * 
 * @author ${classAuthor}
 * @version ${classVersion}, ${.now?string("yyyy-MM-dd HH:mm:ss zzzz")}
 * @see ${packageName}.${moduleName}.web${subModuleName}
 * @since JDK1.7
 */
@Repository
public class ${ClassName}Dao extends BaseDao<${ClassName}> {
	
}
