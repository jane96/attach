/**
 * @(#)CustomJsonFilter.java     1.0 14:52:30
 * Copyright 2015 zxst, Inc. All rights reserved.
 * myattech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Interface CustomJsonFilter.
 * 
 * @author zhaoz
 * @version 1.0,14:52:30
 * @see com.sccl.attech.common.annotation
 * @since JDK1.7
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomJsonFilter {
	
	/**
	 * Mixin.
	 * 
	 * @return the class
	 */
	Class<?> mixin() default Object.class;
	
	/**
	 * Target.
	 * 
	 * @return the class
	 */
	Class<?> target() default Object.class;
}
