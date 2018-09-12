package com.codegenerator.jgen.service;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codegenerator.jgen.database.DatabaseConnection;
import com.codegenerator.jgen.database.DatabaseMetadata;
import com.codegenerator.jgen.model.FMDatabaseMetadata;

@Component
public class DatabaseMetadataService {
	
	@Autowired
	private DatabaseConnection databaseConnection;

	public FMDatabaseMetadata retrieveDatabaseMetadata( ) {
		Connection c = databaseConnection.getConnection();
		DatabaseMetadata dbmd = new DatabaseMetadata();
		FMDatabaseMetadata metadata = dbmd.getDatabaseMetadata(c);
		
		System.out.println("Podaci: "+metadata);
		return metadata;
	}
	
}
