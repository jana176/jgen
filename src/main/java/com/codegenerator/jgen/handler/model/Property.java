package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Property{

	private Visibility visibility;
	
	private String propertyName; // columnName and fkColumnName
	
	private String pkClassName; //pkTableName
	
	private String pkTableName;
	
	private String columnName;
	
	@Builder.Default
	private String cascadeType = null;
	
	@Builder.Default
	private String fetch = "LAZY";
	
	@Builder.Default
	private Boolean orphanRemoval = false;
	
	@Builder.Default
	private Boolean isSelfReferenced = false;
	
}
