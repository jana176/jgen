package com.codegenerator.jgen.handler.model;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Property {

	@NotEmpty
	private Visibility visibility;

	@NotEmpty
	private String propertyName; // columnName and fkColumnName

	@NotEmpty
	private String pkClassName; // pkTableName

	@NotEmpty
	private String pkTableName;

	@NotEmpty
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
