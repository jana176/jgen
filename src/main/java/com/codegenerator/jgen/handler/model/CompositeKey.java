package com.codegenerator.jgen.handler.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompositeKey {
	
	private String tableName;
	
	private List<Field> fields;
	
	private List<Property> properties;
}
