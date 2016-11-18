package com.sccl.attech.common.filter;

import java.util.List;

import org.aspectj.apache.bcel.classfile.annotation.ElementValue;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TemplateJsonFilter {
	@JsonIgnore
	private List<ElementValue>			elementValues;
	@JsonIgnore
	private List<List<ElementValue>>	pageList;
}
