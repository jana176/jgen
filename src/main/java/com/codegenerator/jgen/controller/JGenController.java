package com.codegenerator.jgen.controller;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegenerator.jgen.database.DatabaseConnection;
import com.codegenerator.jgen.database.DatabaseMetadata;

@RestController
@RequestMapping("/jgen")
public class JGenController {

	@Autowired
	private DatabaseConnection databaseConnection;

	@PostMapping("/generate")
	public ResponseEntity<?> generateDatabaseMetadata() throws SQLException {
		Connection c = databaseConnection.getConnection();
		DatabaseMetadata dbmd = new DatabaseMetadata();
		dbmd.getDatabaseMetadata(c);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
