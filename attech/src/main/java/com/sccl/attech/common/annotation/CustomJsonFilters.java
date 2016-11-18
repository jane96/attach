/**
 * @(#)CustomJsonFilters.java     1.0 14:52:35
 * Copyright 2015 zxst, Inc. All rights reserved.
 * myattech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Interface CustomJsonFilters.
 * 
 * @author zhaoz
 * @version 1.0,14:52:35
 * @see com.sccl.attech.common.annotation
 * @since JDK1.7
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomJsonFilters {
	
	/**
	 * Filters.
	 * 
	 * @return the custom json filter[]
	 */
	CustomJsonFilter[] filters();
}
