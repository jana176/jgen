package ${packageName};

<#list imports as package>
import ${package};
</#list>

@Entity
@Table(name = "${class.tableName}")
public class ${class.className?capitalize} {
	<#list fields as field>

		<#if field.isGenerated>
		@GeneratedValue
		</#if>
		<#if field.isEnum>
		@Enumerated(EnumType.STRING)
		</#if>
		<#if field.isPrimaryKey>
		@Id
		public ${field.columnTypeName} ${field.fieldName};
		<#elseif field.foreignKeyInfo??>
		@ManyToOne<#if field.foreignKeyInfo.cascadeType??>(cascade = CascadeType.${field.foreignKeyInfo.cascadeType})</#if>
		public ${field.foreignKeyInfo.pkTableName} ${field.foreignKeyInfo.pkColumnName};
		<#else>
		@Column(name = "${field.columnName}", length = ${field.columnSize}<#if !field.isNullable>, nullable = false</#if><#if field.isUnique>, unique = true</#if>)
		public ${field.columnTypeName} ${field.fieldName};
		</#if>
	</#list>

}