package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseConnection {

	private String driverName;
	private String url;
	private String username;
	private String password;
}
