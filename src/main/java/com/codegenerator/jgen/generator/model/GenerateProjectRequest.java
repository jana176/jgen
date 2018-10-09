package com.codegenerator.jgen.generator.model;

import java.util.List;

import javax.validation.Valid;

import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.DatabaseConnection;

import lombok.Data;

@Data
public class GenerateProjectRequest {

	@Valid
	private NewProjectInfo newProjectInfo;
	
	@Valid
	private DatabaseConnection databaseConnection;
	
	private List<ClassData> classes;
	
}
