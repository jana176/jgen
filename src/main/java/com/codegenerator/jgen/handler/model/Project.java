package com.codegenerator.jgen.handler.model;

import java.util.List;

import javax.validation.Valid;

import com.codegenerator.jgen.generator.model.NewProjectInfo;

import lombok.Data;

@Data
public class Project {

	@Valid
	private NewProjectInfo newProjectInfo;
	
	@Valid
	private DatabaseConnection databaseConnection;
	
	private List<ClassData> classes;
}
