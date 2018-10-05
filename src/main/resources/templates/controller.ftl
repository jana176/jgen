package ${packageName};

<#list imports as package>
import ${package};
</#list>

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${controllerPath}")
public class ${class.className}Controller {

	@Autowired
	private ${class.className}Service ${fieldName}Service;
	
	<#if class.controller.controllerOperations.get && !class.compositeKey??>

	@GetMapping(value = "/${fieldName}/{id}")
    public Optional<${class.className}> get${class.className}(@PathVariable ${idField.type} id) {
        return ${fieldName}Service.findById(id);
    }
    <#elseif class.controller.controllerOperations.get && class.compositeKey??>
    @GetMapping(value = "/${fieldName}")
    public Optional<${class.className}> get${class.className}(<#if compositeKey.fields??><#list compositeKey.fields as field>@RequestParam(value = "${field.columnName}") ${field.columnType} ${field.columnName},</#list></#if><#if compositeKey.properties??><#list compositeKey.properties as property>@RequestParam(value = "${property.columnName}") ${property.columnType} ${property.columnName},</#list></#if>) {
        ${class.className}Id ${class.className?lower_case} = new ${class.className}Id(<#list compositeKey.fields?? as field>${field.columnName}, </#list><#list compositeKey.properties?? as property>${property.columnName}, </#list>);
        return ${fieldName}Service.findById(${class.className?lower_case}); 
    }
	</#if>
	<#if class.controller.controllerOperations.put>
	
	@PutMapping(value = "/${fieldName}/{id}")
    public ResponseEntity<${class.className}> update${class.className}(@RequestBody ${class.className} ${fieldName}, @PathVariable ${idField.type} id) {
		Optional<${class.className}> found${class.className} = ${fieldName}Service.findById(id);
		if(found${class.className}.isPresent()) {
			${fieldName}.setCustomernumber(found${class.className}.get().get${idField.fieldName}());
			${class.className} saved = ${fieldName}Service.save(${fieldName});
			return ResponseEntity.ok(saved);
		}
		else
			return ResponseEntity.notFound().build();
    }
	</#if>
	<#if class.controller.controllerOperations.post>
	
	@PostMapping(value = "/${class.tableName?lower_case}")
    public ${class.className} create${class.className}(@RequestBody ${class.className} ${fieldName}) {
        return ${fieldName}Service.save(${fieldName});
    }
	</#if>
	<#if class.controller.controllerOperations.delete>
	
	@DeleteMapping("/${fieldName}/{id}")
	public void delete${class.className}(@PathVariable ${idField.type} id) {
		${fieldName}Service.delete${class.className}(id);
	}
	</#if>
	
}