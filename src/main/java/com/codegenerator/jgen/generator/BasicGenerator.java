package com.codegenerator.jgen.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class BasicGenerator {

	private GeneratorOptions generatorOptions;
	private String outputPath;	
	private String templateName;
	private String templateDir;
	private String outputFileName;
	private boolean overwrite = false;
	private String filePackage;
	private Configuration cfg;
	private Template template;
	
	
}
