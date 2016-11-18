package com.sccl.attech.common.vo;

public class ZtreeVo {
	
	private String id;
	
	private String pId;
	
	private String name;
	
	private String type; //1表示部门，2表示用户

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ZtreeVo(String id, String pId, String name, String type) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.type = type;
	}

	public ZtreeVo(String id, String pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public ZtreeVo() {
		super();
	}
	
	

}
