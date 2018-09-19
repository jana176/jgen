package com.codegenerator.jgen.model;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NewProjectInfo {

	@NotEmpty
	@JsonProperty("basePath")
	private String path;
	
	@NotEmpty
	private String groupId;
	
	@NotEmpty
	private String artifactId;
	
	@NotEmpty
	private String version;
	
	@NotEmpty
	private String projectName;
	
	private String description;
	
	@NotEmpty
	private String basePackageName;
	
	private String driverName;
	
	private String url;
	
	private String username;
	
	private String password;
}
