/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sccl.attech.modules.sys.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.sccl.attech.common.persistence.IdEntity;

/**
 * 区域Entity
 * @author sccl
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_area")
@DynamicInsert @DynamicUpdate
public class Area extends IdEntity<Area> {

	private static final long serialVersionUID = 1L;
	private Area parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	//private String typeName; //类型名称 （1：国家；2：省份、直辖市；3：地市；4：区县）用于前端显示
	private String parentId;//区域父编号
	private String parentName;//区域父名称
	private List<Office> officeList = Lists.newArrayList(); // 部门列表
	private List<Area> childList = Lists.newArrayList();	// 拥有子区域列表
	
	private String outId; //外部ID
	
	private String label; //用于树显示
	private String hasChildren;//用于树显示

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public Area(){
		super();
	}
	
	public Area(String id){
		this();
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToMany(mappedBy = "area", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Office> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}

	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Area> getChildList() {
		return childList;
	}

	public void setChildList(List<Area> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<Area> list, List<Area> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Area e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Area childe = sourcelist.get(j);
					if (childe.getParent()!=null && childe.getParent().getId()!=null
							&& childe.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}

	@Transient
	public static boolean isAdmin(String id){
		return id != null && id.equals("1");
	}
	
//	@Transient
//	public String getTypeName() {
//		return DictUtils.getDictLabel(type, "sys_area_type", "无");
//	}
	@Transient
	public String getParentId() {
		if(StringUtils.isBlank(this.parentId)){
			if(this.parent!=null){
				return parent.getId();
			}
		}
		return this.parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	//回显时使用
	@Transient
	public String getParentName() {
		if(StringUtils.isBlank(this.parentName)){
			if(this.parent!=null){
				return parent.getName();
			}
		}
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	@Transient
	public String getLabel() {
		this.label=this.name;
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Transient
	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	
}