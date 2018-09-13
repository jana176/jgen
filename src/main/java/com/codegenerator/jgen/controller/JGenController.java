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
import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.generator.ModelGenerator;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;

	@Autowired
	public ModelGenerator classGenerator;

	@PostMapping("/generate")
	public ResponseEntity<?> generateDatabaseMetadata() throws SQLException {

		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		classGenerator.generate(metadata);
		
		System.out.println(ClassNamesUtil.separateEnumValues("('MALE','FEMALE','WHATEVER')"));
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
