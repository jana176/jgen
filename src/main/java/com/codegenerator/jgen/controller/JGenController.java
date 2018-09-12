package com.codegenerator.jgen.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.generator.ModelGenerator;
import com.codegenerator.jgen.model.FMDatabaseMetadata;
import com.codegenerator.jgen.service.DatabaseMetadataService;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;
	
	@Autowired
	public ModelGenerator classGenerator;

	@PostMapping("/generate")
	public ResponseEntity<?> generateDatabaseMetadata() throws SQLException {

		System.out.println(ClassNamesUtil.fromTableToClassName("JANA_JE_LIJEPA"));
		System.out.println(ClassNamesUtil.fromTableToClassName("JANA"));
		System.out.println(ClassNamesUtil.fromColumnNameToFieldName("JANA_JE_LIJEPA"));
		
		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		classGenerator.generate(metadata);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
