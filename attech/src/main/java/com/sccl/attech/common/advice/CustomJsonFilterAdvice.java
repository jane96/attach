/**
 * @(#)CustomJsonFilterAdvice.java     1.0 15:36:58
 * Copyright 2015 zxst, Inc. All rights reserved.
 * myattech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.common.advice;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.sccl.attech.common.annotation.CustomJsonFilter;
import com.sccl.attech.common.annotation.CustomJsonFilters;
import com.sccl.attech.common.mapper.JsonMapper;

/**
 * The Class CustomJsonFilterAdvice. 处理自定义json过滤
 * 
 * @author zhaoz
 * @version 1.0,15:36:58
 * @see com.sccl.attech.common.advice
 * @since JDK1.7
 */
@Component
@Aspect
public class CustomJsonFilterAdvice {

	private HttpServletResponse getResponse(Object[] args) {
		HttpServletResponse response = null;
		for (Object object : args) {
			if (object instanceof HttpServletResponse)
				response = (HttpServletResponse) object;
		}
		return response;
	}
	@Pointcut("@annotation(com.sccl.attech.common.annotation.CustomJsonFilter)||@annotation(com.sccl.attech.common.annotation.CustomJsonFilters)")
	public void methodPointcut() {

	}
	/**
	 * Do around.
	 * 
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("execution(* com.sccl.attech.modules.template.web.*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature msig = (MethodSignature) pjp.getSignature();
		HttpServletResponse response = getResponse(pjp.getArgs());
		CustomJsonFilter annotation = msig.getMethod().getAnnotation(CustomJsonFilter.class);
		CustomJsonFilters annotations = msig.getMethod().getAnnotation(CustomJsonFilters.class);

		if ((annotation == null && annotations == null) || null == response) {
			return pjp.proceed();
		}

		JsonMapper mapper = new JsonMapper();
		if (annotation != null) {
			Class<?> mixin = annotation.mixin();
			Class<?> target = annotation.target();

			if (target != null) {
				mapper.addMixInAnnotations(target, mixin);
			} else {
				mapper.addMixInAnnotations(msig.getMethod().getReturnType(), mixin);
			}
		}

		if (annotations != null) {
			CustomJsonFilter[] filters = annotations.filters();
			for (CustomJsonFilter filter : filters) {
				Class<?> mixin = filter.mixin();
				Class<?> target = filter.target();
				if (target != null) {
					mapper.addMixInAnnotations(target, mixin);
				} else {
					mapper.addMixInAnnotations(msig.getMethod().getReturnType(), mixin);
				}
			}

		}

		try {
//			response.setCharacterEncoding("UTF-8");
			 mapper.writeValue(response.getOutputStream(), pjp.proceed());
			return null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// return null;
	}

}
