package com.codegenerator.jgen.handler.model;

import java.util.List;

import lombok.Data;

@Data
public class ClassData {

	private String tableName;
	private String className;
	private List<Field> fields;
	private List<Enumeration> enums;
	private List<Property> properties;
}
