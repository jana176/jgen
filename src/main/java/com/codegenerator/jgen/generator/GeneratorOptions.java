package com.codegenerator.jgen.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneratorOptions {

	private String templateDir;
	private String templateName;
	private String outputFilePackage;
	private String outputFileName;
	private Boolean overwrite;

}
