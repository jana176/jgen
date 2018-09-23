package com.codegenerator.jgen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PackagePath {
	
	@JsonProperty("packagePath")
	public String path;

}
