package com.codegenerator.jgen.database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codegenerator.jgen.database.model.FMDatabaseMetadata;

@Component
public class DatabaseMetadataService {
	
	@Autowired
	private DatabaseMetadataExtractor databaseMetadataExtractor;

	public FMDatabaseMetadata retrieveDatabaseMetadata( ) {
		FMDatabaseMetadata metadata = databaseMetadataExtractor.getDatabaseMetadata();
		
		System.out.println("Podaci: "+metadata);
		return metadata;
	}
	
}
