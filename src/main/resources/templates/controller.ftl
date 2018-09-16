package generated.controller;

import generated.model.${className};
import generated.service.${className}Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class ${className}Controller {

	@Autowired
	private ${className}Service ${fieldName}Service;
	
	@GetMapping(value = "/${fieldName}/{id}")
    public Optional<${className}> get${className}(@PathVariable("id") ${idType} id) {
        return ${fieldName}Service.findById(id);
    }
    
}