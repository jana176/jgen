package com.codegenerator.jgen.handler.model;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {

	@NotEmpty
	private Visibility visibility;
	
	@NotEmpty
	private String type;
	
	@NotEmpty
	private Integer size;
	
	private Integer precision;
	
	@NotEmpty
	private String fieldName;
	
	@NotEmpty
	private String columnName;
	
	@NotEmpty
	private Boolean isNullable;
	
	@NotEmpty
	private Boolean isPrimaryKey;
	
	@NotEmpty
	private Boolean isUnique;
	
	@NotEmpty
	private Boolean isGenerated;
	
}
