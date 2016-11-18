package com.sccl.attech.common.vo;
/**
 * 手机端辅助类 模板元素
 * @author luoyang
 *
 */
public class MobileElementValueVo {
	
	/** The 项标题. */
	private String				valueTitle;
	
	private String             valueName;
	
	/** The 元素类型. */
	private String				elementType;

	/** The 默认值. */
	private String				defaultValue;

	/** The 是否必填. */
	private char				ismust;
	
	/** The 排序. */
	private Integer				sort;
	
	private String parentId; //父类ID
	
	private String elementId; //元素ID
	
	/** The 其他设置. */
	private String				options;
	
	/** 模板编号 */
	private String            templateId; 
	/** 流程编号 */
	private String   flowDefineId;
	/** 流程节点编号 */
	private String   flowDefineTaskId;
	/** 流程类型编号 */
	private String   flowTypeId;
	
	
	
	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getValueTitle() {
		return valueTitle;
	}

	public void setValueTitle(String valueTitle) {
		this.valueTitle = valueTitle;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public char getIsmust() {
		return ismust;
	}

	public void setIsmust(char ismust) {
		this.ismust = ismust;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getFlowDefineId() {
		return flowDefineId;
	}

	public void setFlowDefineId(String flowDefineId) {
		this.flowDefineId = flowDefineId;
	}

	public String getFlowDefineTaskId() {
		return flowDefineTaskId;
	}

	public void setFlowDefineTaskId(String flowDefineTaskId) {
		this.flowDefineTaskId = flowDefineTaskId;
	}

	public String getFlowTypeId() {
		return flowTypeId;
	}

	public void setFlowTypeId(String flowTypeId) {
		this.flowTypeId = flowTypeId;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	
	
	
}
