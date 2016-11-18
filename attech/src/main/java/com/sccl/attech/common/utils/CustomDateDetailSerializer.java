package com.sccl.attech.common.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
/**
 * jackjson时间格式化有问题，重写时间格式化
 * @author luoyang
 *
 */
public class CustomDateDetailSerializer extends JsonSerializer<Date> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeString(DateUtils.formatDate(value, "yyyy-MM-dd HH:mm:ss")); 
		
	}

}
