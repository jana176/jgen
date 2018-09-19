package com.codegenerator.jgen.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegenerator.jgen.generator.service.MetadataGeneratorService;
import com.codegenerator.jgen.generator.service.ProjectGeneratorService;
import com.codegenerator.jgen.model.NewProjectInfo;
import com.codegenerator.jgen.model.PackagePath;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	public ProjectGeneratorService projectGeneratorService;
	
	@Autowired
	public MetadataGeneratorService metadataGeneratorService;
	
	@PostMapping("/generate")
	public ResponseEntity<?> generateClasses(@RequestBody final PackagePath packagePath) throws SQLException {
		String path = packagePath.getPath().replace("\\\\", "\\");
		metadataGeneratorService.generate(path);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/project")
	public ResponseEntity<?> generateNewProject(@Validated @RequestBody final NewProjectInfo newProjectInfo) throws SQLException {
		String path = newProjectInfo.getPath().replace("\\\\", "\\");
		newProjectInfo.setPath(path);
		String basePackagePath = projectGeneratorService.setUpStructure(newProjectInfo, path);
		System.out.println("1: " + basePackagePath);
		metadataGeneratorService.generate(newProjectInfo, basePackagePath);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
