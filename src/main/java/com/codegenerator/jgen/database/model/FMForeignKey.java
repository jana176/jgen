package com.codegenerator.jgen.database.model;

import lombok.Data;

@Data
public class FMForeignKey {
	
	private String pkTableSchema;
	private String pkTableName;
	private String pkColumnName;
	private String fkTableSchema;
	private String fkTableName;
	private String fkColumnName;
	private String updateRule;
	private String deleteRule;
	private String cascadeType = null;
	private Boolean orphanRemoval = false;

}
