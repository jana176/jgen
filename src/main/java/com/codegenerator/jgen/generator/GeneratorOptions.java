package com.codegenerator.jgen.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratorOptions {

	private String outputPath;
	private String templateName;
	private String templateDir;
	private String outputFileName;
	private Boolean overwrite;
	private String filePackage;

}
