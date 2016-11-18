package com.sccl.attech.modules.sys.vo;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sccl.attech.common.utils.ITreeNode;
import com.sccl.attech.common.utils.excel.annotation.ExcelField;
import com.sccl.attech.modules.sys.entity.Dict;

public class DictVo implements ITreeNode, java.io.Serializable {
	private String id;// id编号
	private Dict parent; // 父级编号
	private String parentIds; // 所有父级编号
	private String label; // 标签名
	private String value; // 数据值
	private String type; // 类型
	private String description;// 描述
	private Integer sort; // 排序
	private String companyId; // 公司ID、0或者空为公有
	private String isParent;// 当前节点下面是否有子节点，是为1，否为0
	private String parentId; // 父Id
	private String parentName;
	private String roles;// 权限
	private int level = 0;
	private boolean leaf;
	public int depth = 0;
	private int treeDep = 0;
	private List<Dict> children;// 孩子部门集合

	public DictVo() {

	}

	public DictVo(String id) {
		this();
		this.id = id;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Dict getParent() {
		return parent;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getParentId() {
		if (StringUtils.isBlank(this.parentId)) {
			if (parent != null) {
				return parent.getId();
			}
		}
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		if (StringUtils.isBlank(this.parentName)) {
			if (parent != null) {
				return parent.getLabel();
			}
		}
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private String hasChildren;

	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<Dict> getChildren() {
		return children;
	}

	public void setChildren(List<Dict> children) {
		this.children = children;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public int getTreeDep() {
		return treeDep;
	}

	public void setTreeDep(int treeDep) {
		this.treeDep = treeDep;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getMyId() {
		return this.id;
	}

	public String getShowName() {
		return this.label;
	}
}
