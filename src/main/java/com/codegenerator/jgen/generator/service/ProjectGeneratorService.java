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

import com.codegenerator.jgen.model.NewProjectInfo;
import com.codegenerator.jgen.model.PackageType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ProjectGeneratorService {

	@Autowired
	public GeneratorService generatorService;

	@Autowired
	public MetadataGeneratorService metadataGeneratorService;

	public String setUpStructure(NewProjectInfo newProjectInfo, String path) {
		StringBuilder sb = new StringBuilder();
		try {
			// create project
			final String projectPath = path + File.separator + newProjectInfo.getProjectName().toString();
			Files.createDirectories(Paths.get(projectPath));
			createPackages(projectPath);
			// create pom file with dependencies
			createPomFile(projectPath, newProjectInfo);
			sb.append(generateApplicationMainClass(projectPath, newProjectInfo));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();

	}

	private void createPomFile(final String projectPath, final NewProjectInfo newProjectInfo) {
		Template template = generatorService.retrieveTemplate(PackageType.POM);
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
		Template template = generatorService.retrieveTemplate(PackageType.APPLICATION);
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

}
