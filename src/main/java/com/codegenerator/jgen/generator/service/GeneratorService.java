package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.GenerateClassesRequest;
import com.codegenerator.jgen.handler.model.GenerateProjectRequest;

@Service
public class GeneratorService {

	@Autowired
	public ModelGeneratorService modelGeneratorService;

	@Autowired
	public RepositoryGeneratorService repositoryGeneratorService;

	@Autowired
	public ServiceGeneratorService serviceGeneratorService;

	@Autowired
	public ControllerGeneratorService controllerGeneratorService;
	
	@Autowired
	public NamingConventionGeneratorService namingConventionGeneratorService;

	public void generate(GenerateProjectRequest generateProjectRequest, String path) {
		String packageName = generateProjectRequest.getNewProjectInfo().getBasePackageName();
		
		if(generateProjectRequest.getDatabaseConnection().getOverrideNamingConvention()) {
			namingConventionGeneratorService.generate(path, packageName);
		}
		List<ClassData> classes = generateProjectRequest.getClasses();
		modelGeneratorService.generate(classes, path, packageName);
		repositoryGeneratorService.generate(classes, path, packageName);
		serviceGeneratorService.generate(classes, path, packageName);
		controllerGeneratorService.generate(classes, path, packageName);
	}

	public void generate(GenerateClassesRequest request, String path) {
		String packageName = "generated";
		String basePath = path + File.separator + packageName;
		List<ClassData> classes = request.getClasses();
		modelGeneratorService.generate(classes, basePath, packageName);
		repositoryGeneratorService.generate(classes, basePath, packageName);
		serviceGeneratorService.generate(classes, basePath, packageName);
		controllerGeneratorService.generate(classes, basePath, packageName);
	}
}
