package ${packageName};

<#list imports as package>
import ${package};
</#list>

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${controllerPath}")
public class ${class.className}Controller {

	@Autowired
	private ${class.className}Service ${fieldName}Service;
	
	<#if class.controller.controllerOperations.get && !class.compositeKey??>

	@GetMapping(value = "/${fieldName}/{id}")
    public ResponseEntity<Optional<${class.className}>> get${class.className}(@PathVariable ${idField.type} id) {
    Optional<${class.className}> ${fieldName} = ${fieldName}Service.findById(id);
        if(${fieldName}.isPresent())
        	return ResponseEntity.ok(${fieldName});
        else
        	return ResponseEntity.notFound().build();
    }
    <#elseif class.controller.controllerOperations.get && class.compositeKey??>
    @GetMapping(value = "/${fieldName}")
    public ResponseEntity<Optional<${class.className}>> get${class.className}(<#if class.compositeKey.fields??><#list class.compositeKey.fields as field>@RequestParam(value = "${field.columnName}") ${field.type} ${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>@RequestParam(value = "${property.columnName}") ${property.type} ${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>) {
        ${class.className}Id ${fieldName}Id = new ${class.className}Id(<#if class.compositeKey.fields??><#list class.compositeKey.fields as field>${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>);
        Optional<${class.className}> ${fieldName} = ${fieldName}Service.findById(${fieldName}Id);
        if(${fieldName}.isPresent())
        	return ResponseEntity.ok(${fieldName});
        else
        	return ResponseEntity.notFound().build();
    }
	</#if>
	<#if class.controller.controllerOperations.put && !class.compositeKey??>

	@PutMapping(value = "/${fieldName}/{id}")
    public ResponseEntity<${class.className}> update${class.className}(@RequestBody ${class.className} ${fieldName}, @PathVariable ${idField.type} id) {
    	Optional<${class.className}> found${class.className} = ${fieldName}Service.findById(id);
		if(found${class.className}.isPresent()) {
			${fieldName}.set${idField.fieldName}(found${class.className}.get().get${idField.fieldName}());
			return ResponseEntity.ok(${fieldName}Service.save(${fieldName}));
		}
		else
			return ResponseEntity.notFound().build();
    }
    <#elseif class.controller.controllerOperations.put && class.compositeKey??>
	@PutMapping(value = "/${fieldName}")
    public ResponseEntity<${class.className}> update${class.className}(@RequestBody ${class.className} ${fieldName}, <#if class.compositeKey.fields??><#list class.compositeKey.fields as field>@RequestParam(value = "${field.columnName}") ${field.type} ${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>@RequestParam(value = "${property.columnName}") ${property.type} ${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>) {
		${class.className}Id ${fieldName}Id = new ${class.className}Id(<#if class.compositeKey.fields??><#list class.compositeKey.fields as field>${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>);
		Optional<${class.className}> found${class.className} = ${fieldName}Service.findById(${fieldName}Id);
		if(found${class.className}.isPresent()) {
			${fieldName}.set${idField.fieldName}(found${class.className}.get().get${idField.fieldName}());
			return ResponseEntity.ok(${fieldName}Service.save(${fieldName}));
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
	<#if class.controller.controllerOperations.delete && !class.compositeKey??>

	@DeleteMapping("/${fieldName}/{id}")
	public ResponseEntity<?> delete${class.className}(@PathVariable ${idField.type} id) {
		Optional<${class.className}> ${fieldName} = ${fieldName}Service.findById(id);
		if (${fieldName}.isPresent()) {
			${fieldName}Service.delete${class.className}(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	<#elseif class.controller.controllerOperations.delete && class.compositeKey??>
	
	@DeleteMapping("/${fieldName}")
	public ResponseEntity<?> delete${class.className}(<#if class.compositeKey.fields??><#list class.compositeKey.fields as field>@RequestParam(value = "${field.columnName}") ${field.type} ${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>@RequestParam(value = "${property.columnName}") ${property.type} ${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>) {
		${class.className}Id ${fieldName}Id = new ${class.className}Id(<#if class.compositeKey.fields??><#list class.compositeKey.fields as field>${field.columnName}, </#list></#if><#if class.compositeKey.properties??><#list class.compositeKey.properties as property>${property.columnName}<#if property!=class.compositeKey.properties?last>, </#if></#list></#if>);
		Optional<${class.className}> ${fieldName} = ${fieldName}Service.findById(${fieldName}Id);
		if (${fieldName}.isPresent()) {
			${fieldName}Service.delete${class.className}(${fieldName}Id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	</#if>
	
}