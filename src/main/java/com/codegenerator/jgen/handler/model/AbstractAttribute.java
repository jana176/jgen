package com.codegenerator.jgen.handler.model;

import javax.validation.constraints.NotEmpty;

import com.codegenerator.jgen.handler.model.enumeration.Visibility;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public abstract class AbstractAttribute {

	@NotEmpty
	protected Visibility visibility;
	
	@NotEmpty
	protected String type;
	
	@NotEmpty
	protected Integer size;
	
	protected Integer precision;
	
	@NotEmpty
	protected String fieldName;
	
	@NotEmpty
	protected String columnName;
	
	@NotEmpty
	protected Boolean isNullable;
	
	@NotEmpty
	protected Boolean isPrimaryKey;
	
	@NotEmpty
	protected Boolean isUnique;
	
	@NotEmpty
	protected Boolean isGenerated;
}
