package com.codegenerator.jgen.handler.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Project {
	
	private DatabaseConnection databaseConnection;
	
	private List<ClassData> classes;
	
}
