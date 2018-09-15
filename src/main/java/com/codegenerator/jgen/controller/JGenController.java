package com.codegenerator.jgen.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.service.DatabaseMetadataService;
import com.codegenerator.jgen.generator.service.ModelGeneratorService;
import com.codegenerator.jgen.generator.service.RepositoryGeneratorService;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;

	@Autowired
	public ModelGeneratorService modelGeneratorService;
	
	@Autowired
	public RepositoryGeneratorService repositoryGeneratorService;

	@PostMapping("/generate")
	public ResponseEntity<?> generateDatabaseMetadata() throws SQLException {

		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		modelGeneratorService.generate(metadata);
		repositoryGeneratorService.generate(metadata);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
