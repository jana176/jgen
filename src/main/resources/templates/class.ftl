package model;

import javax.persistence.*;
<#list class.tableColumns as package>
import ${package.columnName};
</#list>

@Entity
@Table(name = "${class.tableName}")
public class ${class.tableName?capitalize} {
	
	<#list fields as field>
		<#if field.isPrimaryKey>
			@Id
		</#if>
		<#if field.isGenerated>
			@GeneratedValue
		</#if>
		<#if field.isEnum>
			@Enumerated
		</#if>
		@Column(name = "${field.columnName}", <#if !field.isNullable>nullable = false, </#if> <#if field.isUnique>unique = true, </#if>length = ${field.columnSize})
		public ${field.columnTypeName} ${field.columnName}; 
	</#list>

		
}