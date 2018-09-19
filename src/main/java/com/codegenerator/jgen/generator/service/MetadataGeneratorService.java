package com.codegenerator.jgen.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.service.DatabaseMetadataService;
import com.codegenerator.jgen.model.NewProjectInfo;

@Service
public class MetadataGeneratorService {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;

	@Autowired
	public ModelGeneratorService modelGeneratorService;

	@Autowired
	public RepositoryGeneratorService repositoryGeneratorService;

	@Autowired
	public ServiceGeneratorService serviceGeneratorService;

	@Autowired
	public ControllerGeneratorService controllerGeneratorService;

	public void generate(NewProjectInfo newProjectInfo, String path) {


		String packageName = newProjectInfo.getBasePackageName();
		System.out.println("2: " + path);

		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		modelGeneratorService.generate(metadata, path, packageName);
		repositoryGeneratorService.generate(metadata, path, packageName);
		serviceGeneratorService.generate(metadata, path, packageName);
		controllerGeneratorService.generate(metadata, path, packageName);
	}

	public void generate(String path) {
		String packageName = "generated";
		FMDatabaseMetadata metadata = databaseMetadataService.retrieveDatabaseMetadata();
		modelGeneratorService.generate(metadata, path, packageName);
		repositoryGeneratorService.generate(metadata, path, packageName);
		serviceGeneratorService.generate(metadata, path, packageName);
		controllerGeneratorService.generate(metadata, path, packageName);
	}
}
