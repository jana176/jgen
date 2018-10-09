package com.codegenerator.jgen.generator.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.codegenerator.jgen.handler.model.ClassData;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateClassesRequest {

	@NotNull
	public String path;
	
	@NotNull
	private List<ClassData> classes;
	
}
