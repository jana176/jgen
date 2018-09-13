package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.model.PackageType;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class GeneratorService {

	@Autowired
	public FreeMarkerConfigurer freeMarkerConfigurer;

	private static final String BASE_PATH = new File("").getAbsolutePath();

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

	public Writer getAndPrepareWriter(PackageType packageType, String className) throws IOException {
		System.out.println();
		File outputFile = new File(BASE_PATH + File.separator + "src/main/java/generated" + File.separator
				+ packageType.toString().toLowerCase() + File.separator + ClassNamesUtil.toClassName(className)
				+ ".java");
		outputFile.getParentFile().mkdirs();

		return new OutputStreamWriter(new FileOutputStream(outputFile));
	}

	private String determineTemplateName(PackageType type) {
		switch (type) {
		case ENUM:
			return "enum.ftl";
		case MODEL:
			return "class.ftl";
		case REPOSITORY:
			return "repo.ftl";
		case CONTROLLER:
			return "controller.ftl";
		default:
			return "";
		}
	}
}
