package ${packageName};

<#list imports as package>
import ${package};
</#list>
import lombok.Data;

@Data
@Entity
@Table(name = "${class.tableName}")
public class ${class.className?capitalize} {
	<#list class.fields as field>
	
		<#if field.isPrimaryKey>
		@Id
		</#if>
		<#if field.isGenerated>
		@GeneratedValue
		</#if>
		@Column(name = "${field.columnName}"<#if !field.isNullable>, nullable = false</#if><#if field.type=="Double">, precision=${field.size}, scale=${field.precision}<#elseif field.size gt 255>, columnDefinition="TEXT"<#else>, length = ${field.size}</#if>)
		${field.visibility?lower_case} ${field.type} ${field.fieldName};
	</#list>
	<#if class.enums??><#list class.enums as enum>
	
		@Enumerated(EnumType.STRING)
		${enum.visibility?lower_case} ${enum.enumType} ${enum.enumType?lower_case};
	</#list></#if>
	<#if class.properties??><#list class.properties as property>
		<#if property.isSelfReferenced>
		
		@ManyToOne(cascade={CascadeType.ALL})
		@JoinColumn(name="${property.columnName}")
		${property.visibility?lower_case} ${property.pkClassName} ${property.propertyName};

		@OneToMany(mappedBy="${property.propertyName}")
		private Set<${property.pkClassName}> ${property.pkTableName?lower_case} = new HashSet<${property.pkClassName}>();
		<#else>
		
		@ManyToOne(fetch = FetchType.${property.fetch}<#if property.cascadeType??>cascade = CascadeType.${property.cascadeType}</#if>)
		${property.visibility?lower_case} ${property.pkClassName} ${property.propertyName?lower_case};
		</#if>
	</#list></#if>
	
}