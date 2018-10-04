package com.codegenerator.jgen.handler.model;

import com.codegenerator.jgen.handler.model.enumeration.Visibility;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Property extends AbstractAttribute {

	private String pkTableName;

	private String pkClassName;

	private String pkColumnName;

	private String cascadeType;

	private String fetch;

	private Boolean orphanRemoval;

	private Boolean isSelfReferenced;
	
	private Boolean isManyToMany;

	@Builder
	private Property(Visibility visibility, String type, Integer size, Integer precision, String fieldName,
			String columnName, Boolean isNullable, Boolean isPrimaryKey, Boolean isUnique, Boolean isGenerated,
			String pkTableName, String pkClassName, String pkColumnName, String cascadeType, String fetch,
			Boolean orphanRemoval, Boolean isSelfReferenced) {
		this.visibility = visibility;
		this.type = type;
		this.size = size;
		this.precision = precision;
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.isNullable = isNullable;
		this.isPrimaryKey = isPrimaryKey;
		this.isUnique = isUnique;
		this.isGenerated = isGenerated;
		this.pkTableName = pkTableName;
		this.pkClassName = pkClassName;
		this.pkColumnName = pkColumnName;
		this.cascadeType = cascadeType;
		this.fetch = fetch;
		this.orphanRemoval = orphanRemoval;
		this.isSelfReferenced = isSelfReferenced;

	}

}
