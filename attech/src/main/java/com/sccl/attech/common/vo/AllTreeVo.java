package com.sccl.attech.common.vo;

import java.util.List;

import com.sccl.attech.modules.sys.entity.Area;
import com.sccl.attech.modules.sys.entity.Menu;
import com.sccl.attech.modules.sys.entity.Office;

/**
 * @author deng
 * 树 视图 （一次读取所有数据）
 */
public class AllTreeVo {
	private String id; //编号
	private String label; //名称
	private String type;  //类型
	private String roles;//权限

	private String parentIds; //所有父编号字符串 （区域用到）
	private boolean hasChild; // 是否包含孩子节点
	private List<AllTreeVo> children; //孩子节点
	
	
	public AllTreeVo(){
		super();
	}
	
	/**
	 * 区域树
	 * @param area
	 * @param extId
	 */
	public AllTreeVo(Area area,String extId){
		if (extId == null || (extId!=null && !extId.equals(area.getId()) && area.getParentIds().indexOf(","+extId+",")==-1)){
			this.id = area.getId();
			this.label = area.getName();
			this.type = area.getType();
			this.parentIds = area.getParentIds();
			this.hasChild = true;//是否有孩子默认为true;
		}
	}
	
	/**
	 * 部门树
	 * @param office
	 * @param extId
	 */
	public AllTreeVo(Office office,String extId){
		if (extId == null || (extId!=null && !extId.equals(office.getId()) && office.getParentIds().indexOf(","+extId+",")==-1)){
			this.id = office.getId();
			this.label = office.getName();
			this.type = office.getType();
			this.parentIds = office.getParentIds();
			this.hasChild = true;//是否有孩子默认为true;
		}
	}
	
	/**
	 * 菜单树
	 * @param Menu
	 * @param extId
	 */
	public AllTreeVo(Menu menu,String extId){
		if (extId == null || (extId!=null && !extId.equals(menu.getId()) && menu.getParentIds().indexOf(","+extId+",")==-1)){
			this.id = menu.getId();
			this.label = menu.getName();
			this.type = null;
			this.parentIds = menu.getParentIds();
			this.hasChild = true;//是否有孩子默认为true;
		}
	}
	
	/**
	 * 生产公有树结构
	 * @return
	 */
	//public List<AllTreeVo> getAllTreeList(List<E>)
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public List<AllTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<AllTreeVo> children) {
		this.children = children;
	}
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
}
