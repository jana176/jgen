package ${packageName};

<#list imports as package>
import ${package};
</#list>

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${className}Service {

	@Autowired
	private ${className}Repository ${fieldName}Repository;
	
	public  Optional<${className}> findById(${idType} id) {
		return ${fieldName}Repository.findById(id);
	}
	
	public Iterable<${className}> findAll(){
		return ${fieldName}Repository.findAll();
	}

	public ${className} save(${className} ${fieldName}) {
		return ${fieldName}Repository.save(${fieldName});
	}

	public void delete${className}(${idType} id) {
		Optional<${className}> ${fieldName} = findById(id);
		if (${fieldName} != null) {
			${fieldName}Repository.deleteById(id);
		}
	}
}