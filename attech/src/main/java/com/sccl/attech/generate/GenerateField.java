package com.sccl.attech.generate;
/**
 * The Class field.
 * 
 * @author zzz
 * @version 1.0,2015-7-15 16:41:49
 * @see com.sccl.attech.generate
 * @since JDK1.7
 */
public class GenerateField{
	/** The 名称. */
	private String name;
	
	/** The 描述. */
	private String remark;
	
	/**
	 * 获取 the 名称.
	 * 
	 * @return the 名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置 the 名称.
	 * 
	 * @param name the new 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取 the 描述.
	 * 
	 * @return the 描述
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * 设置 the 描述.
	 * 
	 * @param remark the new 描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public GenerateField(String name, String remark) {
		super();
		this.name = name;
		this.remark = remark;
	}
	
}