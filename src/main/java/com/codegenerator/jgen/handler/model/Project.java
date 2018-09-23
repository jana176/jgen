package com.codegenerator.jgen.handler.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Project {

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
	
	private DatabaseConnection databaseConnection;
	
	private List<ClassData> classes;
}
