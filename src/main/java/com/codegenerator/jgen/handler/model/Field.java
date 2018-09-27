package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {

	private Visibility visibility;
	private String type;
	private Integer size;
	private Integer precision;
	private String fieldName;
	private String columnName;
	private Boolean isNullable;
	private Boolean isPrimaryKey;
	private Boolean isUnique;
	private Boolean isGenerated;
	
}
