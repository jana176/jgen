package com.codegenerator.jgen.database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.database.DatabaseMetadataExtractor;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;

@Service
public class DatabaseMetadataService {

	@Autowired
	private DatabaseMetadataExtractor databaseMetadataExtractor;

	public FMDatabaseMetadata retrieveDatabaseMetadata() {
		FMDatabaseMetadata metadata = databaseMetadataExtractor.getDatabaseMetadata();
		System.out.println("Podaci: " + metadata);
		return metadata;
	}

}
