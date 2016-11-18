package com.sccl.attech.common.vo;

/**
 * 流程节点VO
 * @author deng
 *
 */
public class MobileFlowDefineTaskVo {
	private String flowDefineTaskId;
	private String flowDefineTaskName;
	private String templateId;
	private String templateName;
	private String flowDefineId;
	public String getFlowDefineTaskId() {
		return flowDefineTaskId;
	}
	public void setFlowDefineTaskId(String flowDefineTaskId) {
		this.flowDefineTaskId = flowDefineTaskId;
	}
	public String getFlowDefineTaskName() {
		return flowDefineTaskName;
	}
	public void setFlowDefineTaskName(String flowDefineTaskName) {
		this.flowDefineTaskName = flowDefineTaskName;
	}
	public String getFlowDefineId() {
		return flowDefineId;
	}
	public void setFlowDefineId(String flowDefineId) {
		this.flowDefineId = flowDefineId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
}
