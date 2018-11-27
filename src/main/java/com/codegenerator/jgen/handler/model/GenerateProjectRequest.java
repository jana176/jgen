package com.codegenerator.jgen.handler.model;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class GenerateProjectRequest {

	@Valid
	private NewProjectInfo newProjectInfo;
	
	@Valid
	private DatabaseConnection databaseConnection;
	
	private List<ClassData> classes;
	
}
