package com.sccl.attech.modules.sys.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Person {
	
	private String name;
	private String sex;
	private Integer age;
	private Integer id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
