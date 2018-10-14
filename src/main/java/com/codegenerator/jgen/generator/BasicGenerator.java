package com.codegenerator.jgen.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.codegenerator.jgen.generator.model.PackageType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;

@Component
public class BasicGenerator {

	@Autowired
	public FreeMarkerConfigurer freeMarkerConfigurer;

	@Setter
	public String packagePath;

	public Template retrieveTemplate(PackageType packageType) {
		Configuration config = freeMarkerConfigurer.getConfiguration();
		Template template = null;
		String templateName = determineTemplateName(packageType);
		try {
			template = config.getTemplate(templateName);
		} catch (IOException e) {
			System.out.println("Can't find template " + e);
		}
		return template;
	}

	public Writer getAndPrepareWriter(final String packagePath) throws IOException {
		File outputFile = new File(packagePath);
		outputFile.getParentFile().mkdirs();

		return new OutputStreamWriter(new FileOutputStream(outputFile));
	}

	private String determineTemplateName(PackageType type) {
		switch (type) {
		case ENUMERATION:
			return "enum.ftl";
		case MODEL:
			return "class.ftl";
		case EMBEDDED_KEY:
			return "embeddedkey.ftl";
		case REPOSITORY:
			return "repository.ftl";
		case SERVICE:
			return "service.ftl";
		case CONTROLLER:
			return "controller.ftl";
		case POM:
			return "pom.ftl";
		case APPLICATION:
			return "application.ftl";
		case YAML:
			return "yaml.ftl";
		case NAMING_STRATEGY:
			return "naming.ftl";
		default:
			return "";
		}
	}

}
