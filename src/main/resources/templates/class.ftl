package ${packageName};

<#list imports as package>
import ${package};
</#list>
import lombok.Data;

@Data
@Entity
@Table(name = "${class.tableName}")
public class ${class.className} {
<#if class.hasCompositeId>

	@EmbeddedId
	private ${class.className}Id ${class.className?lower_case}Id;
</#if>
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
	${property.visibility?lower_case} ${property.pkClassName} ${property.fieldName};

	@OneToMany(mappedBy="${property.fieldName}")
	private Set<${property.pkClassName}> ${property.pkTableName?lower_case} = new HashSet<${property.pkClassName}>();
	<#elseif class.manyToManyProperty??>
	
	@ManyToMany(<#if class.manyToManyProperty.cascadeType??>cascade = CascadeType.${class.manyToManyProperty.cascadeType}<#else>cascade = CascadeType.ALL</#if>)
	@JoinTable(name = "${class.manyToManyProperty.pkTableName}", joinColumns = { @JoinColumn(name = "${class.manyToManyProperty.columnName}") }, inverseJoinColumns = { @JoinColumn(name = "${class.manyToManyProperty.pkColumnName}") })
	${class.manyToManyProperty.visibility?lower_case} List<${class.manyToManyProperty.pkClassName}> ${class.manyToManyProperty.pkClassName?lower_case} = new ArrayList<>();
	<#else>
	
	@ManyToOne(fetch = FetchType.${property.fetch}<#if property.cascadeType??>cascade = CascadeType.${property.cascadeType}</#if>)
	@JoinColumn(name="${property.pkColumnName}")
	${property.visibility?lower_case} ${property.pkClassName} ${property.fieldName?lower_case};
	</#if>
</#list></#if>
	
}