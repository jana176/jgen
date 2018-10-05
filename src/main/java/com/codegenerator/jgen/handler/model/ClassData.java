package com.codegenerator.jgen.handler.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassData {

	@NotEmpty
	private String tableName;
	
	private String className;
	
	private List<Field> fields;
	
	private List<Enumeration> enums;
	
	private List<Property> properties;
	
	private List<String> compositePks;
	
	private CompositeKey compositeKey;
	
	private Property manyToManyProperty;
	
	private Relationship relationship;
	
	@Builder.Default
	private Boolean generateRepository = true;
	
	private Service service;
	
	private Controller controller;
}
