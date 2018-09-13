package generated.model;

<#list imports as package>
import ${package};
</#list>

@Entity
@Table(name = "${class.tableName}")
public class ${class.tableName?capitalize} {
	
	<#list fields as field>
	
		<#if field.isPrimaryKey>
		@Id
		<#else>
		<#if field.isGenerated>
		@GeneratedValue
		</#if>
		<#if field.isEnum>
		@Enumerated(EnumType.STRING)
		</#if>
		@Column(name = "${field.columnName}", length = ${field.columnSize}<#if !field.isNullable>, nullable = false</#if><#if field.isUnique>, unique = true</#if>)
		</#if>
		public ${field.columnTypeName} ${field.fieldName};
	</#list>

}