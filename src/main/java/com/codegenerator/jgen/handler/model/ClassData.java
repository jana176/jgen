package com.codegenerator.jgen.handler.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassData {

	private String tableName;
	
	private String className;
	
	private List<Field> fields;
	
	private List<Enumeration> enums;
	
	private List<Property> properties;
	
	private Boolean relationshipClass;
	
	@Builder.Default
	private Boolean generateRepository = true;
	
	private Service service;
	
	private Controller controller;
}
