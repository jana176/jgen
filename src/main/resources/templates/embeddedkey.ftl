package ${packageName};

<#list imports as package>
import ${package};
</#list>
import lombok.Getter;

@Embeddable
@Getter
public class ${class.className}Id implements Serializable {

	private static final long serialVersionUID = 1L;
	<#list compositeKey.fields as field>
	
	@Column(name = "${field.columnName}")
    private ${field.type} ${field.fieldName};
	</#list>
	<#list compositeKey.properties as property>
	
	@Column(name = "${property.columnName}")
    private ${property.type} ${property.fieldName};
	</#list>
	
	
}