package com.sccl.attech.modules.sys.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户定位中心
 * @author luoyang
 *
 */
public class UserVo {
	
	private String id;
	
	private String name;	// 姓名
	
	private String email;	// 邮箱
	
	private String mobile;	// 手机
	
	private String locationOnText; //激活定位（0激活定位 1取消定位）
	
	private String photo; //头像
	
	private String officeName;
	
	private Double xaxis; //X轴
	private Double yaxis; //y轴
	private Date locationDate; //定位时间
	private String locationDesc; //位置描述

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocationOnText() {
		return locationOnText;
	}

	public void setLocationOnText(String locationOnText) {
		this.locationOnText = locationOnText;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	

	public Double getXaxis() {
		return xaxis;
	}

	public void setXaxis(Double xaxis) {
		this.xaxis = xaxis;
	}

	public Double getYaxis() {
		return yaxis;
	}

	public void setYaxis(Double yaxis) {
		this.yaxis = yaxis;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLocationDate() {
		return locationDate;
	}

	public void setLocationDate(Date locationDate) {
		this.locationDate = locationDate;
	}
	
	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public UserVo(String id, String name, String email, String mobile,
			 String photo, String officeName) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.photo = photo;
		this.officeName = officeName;
	}

	public UserVo() {
		super();
	}
	
	

}
