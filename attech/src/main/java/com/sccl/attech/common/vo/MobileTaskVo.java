package com.sccl.attech.common.vo;

import java.util.List;

/**
 * 手机端 任务辅助类
 * @author luoyang
 *
 */
public class MobileTaskVo {
	
	private String projectName; //项目名称
	
	private String projectId; //项目Id 未加
	
	private String excuseId;//手机端执行者ID
	
	private String excuseName;//手机端执行者名称
	
	private String taskId; //手机端任务ID
	
	private String taskName; //任务名称
	
	private String endTime; //截止时间
	
	private String address; //目的地
	
	private String tempalteId; //模板ID
	
	private String templateName; //模板名称
	
	private String flowName; //流程模板
	
	private String flowId; //流程ID
	
	private String taskDestription; //任务描述
	
	private char fixedProcess; //流程类型 0：固定；1：不固定
	
	private String flowNodeId;//节点id
	
	private String instanceId;//流程实例ID
	
	private String companyId;//公司Id
	
	private String createBy; //创建人  
	
	private String state;// 0暂存 1下发、待收 2已接收未处理 3退回 4取消任务 5处理中 6完成
	
	private List<MobileElementValueVo> mobileElementValueVos;
	
	private List<MobileExcutorVo> excutorList;//执行者
	
	private String source;// 来源标识（0：手机端；1：电脑端）
	
	private String flowTaskId;
	
	public String getExcuseId() {
		return excuseId;
	}

	public void setExcuseId(String excuseId) {
		this.excuseId = excuseId;
	}

	public String getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(String flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<MobileElementValueVo> getMobileElementValueVos() {
		return mobileElementValueVos;
	}

	public void setMobileElementValueVos(
			List<MobileElementValueVo> mobileElementValueVos) {
		this.mobileElementValueVos = mobileElementValueVos;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getTaskDestription() {
		return taskDestription;
	}

	public void setTaskDestription(String taskDestription) {
		this.taskDestription = taskDestription;
	}

	public char getFixedProcess() {
		return fixedProcess;
	}

	public void setFixedProcess(char fixedProcess) {
		this.fixedProcess = fixedProcess;
	}

	public String getTempalteId() {
		return tempalteId;
	}

	public void setTempalteId(String tempalteId) {
		this.tempalteId = tempalteId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<MobileExcutorVo> getExcutorList() {
		return excutorList;
	}

	public void setExcutorList(List<MobileExcutorVo> excutorList) {
		this.excutorList = excutorList;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExcuseName() {
		return excuseName;
	}

	public void setExcuseName(String excuseName) {
		this.excuseName = excuseName;
	}

	public String getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	@Override
	public String toString() {
		return "MobileTaskVo [projectName=" + projectName + ", projectId="
				+ projectId + ", excuseId=" + excuseId + ", excuseName="
				+ excuseName + ", taskId=" + taskId + ", taskName=" + taskName
				+ ", endTime=" + endTime + ", address=" + address
				+ ", tempalteId=" + tempalteId + ", templateName="
				+ templateName + ", flowName=" + flowName + ", flowId="
				+ flowId + ", taskDestription=" + taskDestription
				+ ", fixedProcess=" + fixedProcess + ", flowNodeId="
				+ flowNodeId + ", instanceId=" + instanceId + ", companyId="
				+ companyId + ", createBy=" + createBy + ", state=" + state
				+ ", mobileElementValueVos=" + mobileElementValueVos
				+ ", excutorList=" + excutorList + ", source=" + source
				+ ", flowTaskId=" + flowTaskId + "]";
	}
	
	
	
}
