package com.codegenerator.jgen.database.model;

import java.util.List;

import lombok.Data;

@Data
public class FMColumn {

	private String tableName;
	private String columnName;
	private String fieldName;
	private String columnTypeName;
	private Integer columnSize;
	private Integer decimalDigits;
	private String columnDefault;
	private Boolean isNullable;
	private Boolean isAutoincrement;
	private Boolean isPrimaryKey = false;
	private Boolean isUnique = false;
	private Boolean isGenerated = false;
	private Boolean isEnum = false;
	private List<String> enumValues;
	private FMForeignKey foreignKeyInfo;
}
