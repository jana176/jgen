package com.codegenerator.jgen.generator.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.codegenerator.jgen.handler.model.ClassData;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateClassesRequest {

	@NotNull
	public String path;
	
	@NotNull
	private List<ClassData> classes;
	
}
