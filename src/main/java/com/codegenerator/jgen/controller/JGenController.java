package com.codegenerator.jgen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegenerator.jgen.controller.validator.ProjectRequestValidator;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.service.DatabaseMetadataService;
import com.codegenerator.jgen.generator.model.GenerateClassesRequest;
import com.codegenerator.jgen.generator.model.GenerateProjectRequest;
import com.codegenerator.jgen.generator.model.GeneratorType;
import com.codegenerator.jgen.generator.service.GeneratorService;
import com.codegenerator.jgen.generator.service.ProjectGeneratorService;
import com.codegenerator.jgen.handler.Handler;
import com.codegenerator.jgen.handler.model.Project;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	public ProjectGeneratorService projectGeneratorService;
	
	@Autowired
	public GeneratorService generatorService;
	
	@Autowired
	public DatabaseMetadataService databaseMetadataService;
	
	@Autowired
	public Handler handler;
	
	@Autowired
	public ProjectRequestValidator projectRequestValidator;
	
	@GetMapping("/metadata/raw")
	public ResponseEntity<FMDatabaseMetadata> retrieveMetadata() {
		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		
		return new ResponseEntity<FMDatabaseMetadata>(metadata, HttpStatus.OK);
	}
	
	@PostMapping("/metadata/transform")
	public ResponseEntity<Project> handleMetadata(@RequestHeader final GeneratorType type, @RequestBody final FMDatabaseMetadata metadata) {
		Project projectInfo = handler.metadataToObjects(metadata, type);
		return new ResponseEntity<Project>(projectInfo, HttpStatus.OK);
	}
	
	@PostMapping("/generate/project")
	public ResponseEntity<?> generateNewProject(@Validated @RequestBody final GenerateProjectRequest generateProjectRequest) {
		String path = generateProjectRequest.getNewProjectInfo().getBasePath().replace("\\\\", "\\");
		generateProjectRequest.getNewProjectInfo().setBasePath(path);
		
		projectRequestValidator.validate(generateProjectRequest.getClasses());
		
		String basePackagePath = projectGeneratorService.setUpStructure(generateProjectRequest.getNewProjectInfo(), generateProjectRequest.getDatabaseConnection(), path);
		System.out.println(basePackagePath);
		generatorService.generate(generateProjectRequest, basePackagePath);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/generate/classes")
	public ResponseEntity<?> generateProjectClasses(@Validated @RequestBody final GenerateClassesRequest request) {
		String path = request.getPath().replace("\\\\", "\\");
		
		projectRequestValidator.validate(request.getClasses());
		
		generatorService.generate(request, path);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
//	@PostMapping("/generate")
//	public ResponseEntity<?> generateClasses(@RequestBody final PackagePath packagePath) throws SQLException {
//		String path = packagePath.getPath().replace("\\\\", "\\");
//		metadataGeneratorService.generate(path);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@PostMapping("/project")
//	public ResponseEntity<?> generateNewProject(@Validated @RequestBody final NewProjectInfo newProjectInfo) throws SQLException {
//		String path = newProjectInfo.getPath().replace("\\\\", "\\");
//		newProjectInfo.setPath(path);
//		String basePackagePath = projectGeneratorService.setUpStructure(newProjectInfo, path);
//		metadataGeneratorService.generate(newProjectInfo, basePackagePath);
//		
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

}
