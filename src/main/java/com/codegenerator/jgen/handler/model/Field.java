package com.codegenerator.jgen.handler.model;

import com.codegenerator.jgen.handler.model.enumeration.Visibility;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Field extends AbstractAttribute {

	@Builder
	private Field(Visibility visibility, String type, Integer size, Integer precision, String fieldName,
			String columnName, Boolean isNullable, Boolean isPrimaryKey, Boolean isUnique, Boolean isGenerated) {
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
	}

}
