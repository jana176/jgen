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
import com.codegenerator.jgen.generator.model.GeneratorType;
import com.codegenerator.jgen.generator.service.GeneratorService;
import com.codegenerator.jgen.generator.service.ProjectGeneratorService;
import com.codegenerator.jgen.handler.Handler;
import com.codegenerator.jgen.handler.model.GenerateClassesRequest;
import com.codegenerator.jgen.handler.model.GenerateProjectRequest;

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
	public ResponseEntity<?> handleMetadata(@RequestHeader final GeneratorType type,
			@RequestBody final FMDatabaseMetadata metadata) {
		if (type == GeneratorType.NEW_PROJECT) {
			GenerateProjectRequest generateProjectRequest = handler.metadataToProjectObjects(metadata);
			return new ResponseEntity<GenerateProjectRequest>(generateProjectRequest, HttpStatus.OK);
		} else if (type == GeneratorType.EXISTING_PROJECT) {
			GenerateClassesRequest generateClassesRequest = handler.metadataToClassesObjects(metadata);
			return new ResponseEntity<GenerateClassesRequest>(generateClassesRequest, HttpStatus.OK);
		}
		else return ResponseEntity.notFound().build();
	}

	@PostMapping("/generate/project")
	public ResponseEntity<?> generateNewProject(
			@Validated @RequestBody final GenerateProjectRequest generateProjectRequest) {
		String path = generateProjectRequest.getNewProjectInfo().getBasePath().replace("\\\\", "\\");
		generateProjectRequest.getNewProjectInfo().setBasePath(path);

		projectRequestValidator.validate(generateProjectRequest.getClasses());

		String basePackagePath = projectGeneratorService.setUpStructure(generateProjectRequest.getNewProjectInfo(),
				generateProjectRequest.getDatabaseConnection(), path);

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

}
