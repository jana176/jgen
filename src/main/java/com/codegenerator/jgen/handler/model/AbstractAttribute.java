package com.codegenerator.jgen.handler.model;

import javax.validation.constraints.NotEmpty;

import com.codegenerator.jgen.handler.model.enumeration.Visibility;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public abstract class AbstractAttribute {

	@NotEmpty
	protected Visibility visibility;
	
	@NotEmpty
	protected String type;
	
	@NotEmpty
	protected Integer size;
	
	protected Integer precision;
	
	@Setter
	@NotEmpty
	protected String fieldName;
	
	@Setter
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
