package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.PackageType;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Relationship;
import com.codegenerator.jgen.handler.model.enumeration.RelationshipType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class RepositoryGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;

	private List<String> imports = new ArrayList<>();

	public void generate(List<ClassData> classes, String path, String packageName) {
		List<ClassData> classesToGenerateRepositoryFor = classes.stream().filter(
				classData -> classData.getGenerateRepository() && generateRepository(classData.getRelationship()))
				.collect(Collectors.toList());

		classesToGenerateRepositoryFor.forEach(classData -> {
			generateRepositoryForModelClass(classData, path, packageName);
		});
	}

	private void generateRepositoryForModelClass(ClassData classData, String path, String packageName) {
		final String idType = retrieveIdColumnType(classData, packageName);
		imports.add(packageName + ".model." + classData.getClassName());

		Template template = basicGenerator.retrieveTemplate(PackageType.REPOSITORY);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator
					.getAndPrepareWriter(path + File.separator + PackageType.REPOSITORY.toString().toLowerCase()
							+ File.separator + classData.getClassName().concat("Repository") + ".java");
			context.clear();
			context.put("repoClassName", classData.getClassName());
			context.put("idType", idType);
			context.put("packageName", packageName.concat(".repository"));
			context.put("imports", imports);
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
		imports.clear();

	}

	private String retrieveIdColumnType(ClassData classData, String packageName) {
		if (classData.getCompositeKey() == null) {
			final Optional<Field> idColumn = classData.getFields().stream().filter(field -> field.getIsPrimaryKey())
					.findAny();
			return idColumn.get().getType();
		} else {
			imports.add(packageName + ".model." + classData.getClassName() + "Id");
			return classData.getClassName() + "Id";
		}
	}
	
	private Boolean generateRepository(Relationship relationship) {
		if((relationship.getRelationshipType() != null && relationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY_SEPARATE_CLASS)) || relationship.getRelationshipType() == null) {
			return true;
		} else {
			return false;
		}
	}

}
