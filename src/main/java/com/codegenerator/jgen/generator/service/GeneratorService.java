package com.codegenerator.jgen.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.handler.model.Project;

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

	public void generate(Project project, String path) {
		String packageName = project.getNewProjectInfo().getBasePackageName();
		
		if(project.getDatabaseConnection().getOverrideNamingConvention()) {
			namingConventionGeneratorService.generate(path, packageName);
		}
		
		modelGeneratorService.generate(project, path, packageName);
		repositoryGeneratorService.generate(project, path, packageName);
		serviceGeneratorService.generate(project, path, packageName);
		controllerGeneratorService.generate(project, path, packageName);
	}

//	public void generate(String path) {
//		String packageName = "generated";
//		String basePath = path + File.separator + packageName;
//		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
//		modelGeneratorService.generate(metadata, basePath, packageName);
//		repositoryGeneratorService.generate(metadata, basePath, packageName);
//		serviceGeneratorService.generate(metadata, basePath, packageName);
//		controllerGeneratorService.generate(metadata, basePath, packageName);
//	}
}
