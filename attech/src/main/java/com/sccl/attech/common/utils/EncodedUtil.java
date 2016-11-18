package com.sccl.attech.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
/**
 * 解析编码
 * @author luoyang
 *
 */
public class EncodedUtil {
	/**
	 * 解析编码
	 * @param value 解析的值
	 * @return
	 */
	public static String encodeValue(String value){
		try {
			
			value=Encodes.unescapeHtml(value);
			value = new String(value.getBytes("iso-8859-1"), "utf-8");
			
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return value;

	}
	/**
	 * 解析编码 解析成UTF-8
	 * @param value 解析值 
	 * @return
	 */
	public static String decodeValue(String value){
		
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
