package com.codegenerator.jgen.database;


import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {

	@Autowired
	private DataSource dataSource;

        
	public Connection getConnection() {
		Connection connection = null;
		try {
			System.out.println(dataSource.toString());
			connection = dataSource.getConnection();
			System.out.println("Successfully connected to database.");
		} catch (Exception e) {
			System.out.println("Failed to create the database connection. " + e);
		}
		return connection;
	}
	
}
