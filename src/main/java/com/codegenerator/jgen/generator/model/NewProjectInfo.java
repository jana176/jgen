package com.codegenerator.jgen.generator.model;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class NewProjectInfo {

	@NotEmpty
	private String basePath;
	
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
	
}
