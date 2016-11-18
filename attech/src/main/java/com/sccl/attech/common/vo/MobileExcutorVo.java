package com.sccl.attech.common.vo;

import java.util.List;

/**
 * 手机端任务执行者视图
 * @author deng
 *
 */
public class MobileExcutorVo {
	
	private String templateId;//模板Id
	private String excutorId;//执行者
	private String taskId;//任务id
	private String exampleId;//模板数据id
	/** The 完成情况. */
	private String	completion; 
	
	private String finishTime;//完成时间
	/** The 上报位置. */
	private String	locations;
	
	private String x; //x坐标
	private String y; //y坐标
	
	private String flowNodeId;//节点id
	
	private String instanceId;//流程实例ID
	
	private List<MoblieElementValueVo> mevvList; //模板数据集合
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getExcutorId() {
		return excutorId;
	}

	public void setExcutorId(String excutorId) {
		this.excutorId = excutorId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExampleId() {
		return exampleId;
	}

	public void setExampleId(String exampleId) {
		this.exampleId = exampleId;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
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

	public List<MoblieElementValueVo> getMevvList() {
		return mevvList;
	}

	public void setMevvList(List<MoblieElementValueVo> mevvList) {
		this.mevvList = mevvList;
	}

	@Override
	public String toString() {
		return "MobileExcutorVo [templateId=" + templateId + ", excutorId="
				+ excutorId + ", taskId=" + taskId + ", exampleId=" + exampleId
				+ ", completion=" + completion + ", finishTime=" + finishTime
				+ ", locations=" + locations + ", x=" + x + ", y=" + y
				+ ", flowNodeId=" + flowNodeId + ", instanceId=" + instanceId
				+ ", mevvList=" + mevvList + "]";
	}
	
	
	
}
