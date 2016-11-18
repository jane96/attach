/**
 * @(#)MapVo.java     1.0 15:43:12
 * Copyright 2015 zxst, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.common.vo;

import java.util.Map;

/**
 * The Class MapVo.
 * 用于接收map参数
 * @author zzz
 * @version 1.0,15:43:12
 * @see com.sccl.attech.common.vo
 * @since JDK1.7
 */
public class MapVo {

	/** The map vo. */
	private Map<String,String>	mapVo;

	/**
	 * Gets the map vo.
	 * 
	 * @return the map vo
	 */
	public Map<String, String> getMapVo() {
		return mapVo;
	}

	/**
	 * 设置 map vo.
	 * 
	 * @param mapVo the map vo
	 */
	public void setMapVo(Map<String, String> mapVo) {
		this.mapVo = mapVo;
	}

}