package ${packageName};

<#list imports as package>
import ${package};
</#list>

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${class.className}Service {

	@Autowired
	private ${class.className}Repository ${fieldName}Repository;
	<#if class.service.serviceOperations.findById>
	
	public  Optional<${class.className}> findById(${idType} id) {
		return ${fieldName}Repository.findById(id);
	}
	</#if>
	<#if class.service.serviceOperations.findById>
	
	public Iterable<${class.className}> findAll(){
		return ${fieldName}Repository.findAll();
	}
	</#if>
	<#if class.service.serviceOperations.findById>
	
	public ${class.className} save(${class.className} ${fieldName}) {
		return ${fieldName}Repository.save(${fieldName});
	}
	</#if>
	<#if class.service.serviceOperations.findById>
	
	public void delete${class.className}(${idType} id) {
		Optional<${class.className}> ${fieldName} = findById(id);
		if (${fieldName} != null) {
			${fieldName}Repository.deleteById(id);
		}
	}
	</#if>
	
}