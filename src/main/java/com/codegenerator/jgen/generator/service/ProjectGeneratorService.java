package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.enumeration.PackageType;
import com.codegenerator.jgen.handler.model.DatabaseConnection;
import com.codegenerator.jgen.handler.model.NewProjectInfo;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ProjectGeneratorService extends BasicGenerator {

	@Autowired
	public GeneratorService generatorService;

	public String setUpStructure(NewProjectInfo newProjectInfo, DatabaseConnection database, String path) {
		StringBuilder sb = new StringBuilder();
		try {
			// create project
			final String projectPath = path + File.separator + newProjectInfo.getProjectName().toString();
			Files.createDirectories(Paths.get(projectPath));
			createPackages(projectPath);
			// create pom file with dependencies
			createPomFile(projectPath, newProjectInfo);
			String basepath = generateApplicationMainClass(projectPath, newProjectInfo);
			sb.append(basepath);
			createYamlFile(projectPath, database, newProjectInfo.getBasePackageName());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();

	}

	private void createPomFile(final String projectPath, final NewProjectInfo newProjectInfo) {
		Template template = retrieveTemplate(PackageType.POM);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		File outputFile = new File(projectPath + File.separator + "pom.xml");
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.clear();
			context.put("project", newProjectInfo);
			template.process(context, out);
			out.flush();
		} catch (TemplateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createPackages(final String projectPath) {
		try {
			Files.createDirectories(Paths.get(projectPath + File.separator + "src/main/java"));
			Files.createDirectories(Paths.get(projectPath + File.separator + "src/main/resources"));
			Files.createDirectories(Paths.get(projectPath + File.separator + "src/test/java"));
			Files.createDirectories(Paths.get(projectPath + File.separator + "src/test/resources"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String generateApplicationMainClass(final String projectPath, final NewProjectInfo newProjectInfo) {
		Template template = retrieveTemplate(PackageType.APPLICATION);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		String mainPackagePath = projectPath + "\\src\\main\\java\\"
				+ newProjectInfo.getBasePackageName().replace(".", "\\");
		File outputFile = new File(mainPackagePath + File.separator + "Application.java");
		System.out.println(mainPackagePath);
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.clear();
			context.put("project", newProjectInfo);
			template.process(context, out);
			out.flush();
		} catch (TemplateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mainPackagePath;
	}

	private void createYamlFile(String path, DatabaseConnection database, String basePackageName) {
		Template template = retrieveTemplate(PackageType.YAML);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		File outputFile = new File(path + "\\src\\main\\resources" + File.separator + "application.yml");
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.clear();
			context.put("database", database);
			context.put("package", basePackageName);
			template.process(context, out);
			out.flush();
		} catch (TemplateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
