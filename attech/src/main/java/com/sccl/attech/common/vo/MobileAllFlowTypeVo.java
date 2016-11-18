package com.sccl.attech.common.vo;

import java.util.List;


/**
 * 手机端接口数据(获取流程类型，流程，流程节点，流程节点对应表单的数据)
 * @author deng
 *
 */
public class MobileAllFlowTypeVo {
	
    /**
     * 流程类型集合
     */
    private List<MobileFlowTypeVo> flowTypeList ;
    /**
     * 流程定义集合
     */
    private List<MobileFlowDefineVo> fdList;
    
    /**
     * 流程定义中的节点集合
     */
    private List<MobileFlowDefineTaskVo> fdtList;
    
    
    /**
     * 流程定义中节点中集合中模板中的元素集合
     */
    private List<MobileElementValueVo> mevvList;
    
	public List<MobileFlowDefineVo> getFdList() {
		return fdList;
	}

	public void setFdList(List<MobileFlowDefineVo> fdList) {
		this.fdList = fdList;
	}

	public List<MobileFlowDefineTaskVo> getFdtList() {
		return fdtList;
	}

	public void setFdtList(List<MobileFlowDefineTaskVo> fdtList) {
		this.fdtList = fdtList;
	}

	public List<MobileElementValueVo> getMevvList() {
		return mevvList;
	}

	public void setMevvList(List<MobileElementValueVo> mevvList) {
		this.mevvList = mevvList;
	}

	public List<MobileFlowTypeVo> getFlowTypeList() {
		return flowTypeList;
	}

	public void setFlowTypeList(List<MobileFlowTypeVo> flowTypeList) {
		this.flowTypeList = flowTypeList;
	}
    
}
