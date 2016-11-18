package com.sccl.attech.modules.sys.entity;

import java.util.List;

public class TreeVO 
{
	//主键ID
	private String id;
	//是否拥有下级
	private String hasChildren;
	//树中显示的名称
	private String label;
	//节点的类型（自定义）
	private String name;
	//节点的类型（自定义）
	private String type;
	//人员list
	private List<User> users;
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
