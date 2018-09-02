package com.codegenerator.jgen.model;

import java.util.List;

import lombok.Data;

@Data
public class FMTable {

	private String tableSchema;
	private String tableName;
	private String tableType;
	private List<FMColumn> tableColumns;
	private List<FMForeignKey> foreignKeys;
	private List<String> uniqueColumns;

}
