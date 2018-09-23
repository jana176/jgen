package com.codegenerator.jgen.handler.model;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseConnection {
	
	@NotEmpty
	private String driverName;
	
	@NotEmpty
	private String url;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
}
