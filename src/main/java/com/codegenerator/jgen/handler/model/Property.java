package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Property{

	private Visibility visibility;
	
	private String classTypeName;
	
	private String propertyName;
	
	@Builder.Default
	private String cascadeType = null;
	
	@Builder.Default
	private Boolean orphanRemoval = false;
	
}
