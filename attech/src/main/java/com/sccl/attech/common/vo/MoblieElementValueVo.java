package com.sccl.attech.common.vo;

/**
 * @author deng
 * 手机端同步数据 模板元素值Vo
 */
public class MoblieElementValueVo {
	
	private String key;
	
	private String keyValue;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public String toString() {
		return "MoblieElementValueVo [key=" + key + ", keyValue=" + keyValue
				+ "]";
	}
	
	

}
