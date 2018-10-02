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

import com.codegenerator.jgen.database.model.FMForeignKey;
import com.codegenerator.jgen.generator.model.PackageType;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Enumeration;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Project;
import com.codegenerator.jgen.handler.model.Property;
import com.codegenerator.jgen.handler.model.enumeration.RelationshipType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ModelGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;

	@Autowired
	public EnumGeneratorService enumGeneratorService;

	private List<String> imports = new ArrayList<>();

	public void generate(Project project, String path, String packageName) {
		List<ClassData> classesToGenerateModelFor = project.getClasses().stream()
				.filter(classData -> !classData.getRelationship().getIsRelationshipClass())
				.collect(Collectors.toList());
		project.getClasses().forEach(classData -> {
			// ako je u pitanju posebna klasa
			if (classData.getRelationship().getRelationshipType() != null && classData.getRelationship()
					.getRelationshipType().equals(RelationshipType.MANY_TO_MANY_SEPARATE_CLASS)) {
				classesToGenerateModelFor.add(classData);
			}
		});

		List<ClassData> m2mclasses = project.getClasses().stream()
				.filter(classData -> classData.getRelationship().getIsRelationshipClass()).collect(Collectors.toList());
		project.getClasses().forEach(classData -> {
			// ako je u pitanju posebna klasa
			if (classData.getRelationship().getRelationshipType() != null
					&& classData.getRelationship().getRelationshipType().equals(RelationshipType.MANY_TO_MANY)) {
				m2mclasses.add(classData);
			}
		});
		m2mclasses.forEach(classData -> handleManyToManyInfo(project.getClasses(), classData));

		classesToGenerateModelFor.forEach(classData -> {
			generateModelClass(classData, path, packageName);
		});
	}

	private void generateModelClass(ClassData classData, String path, String packageName) {
		prepareImports(classData);
		determineEnums(classData, path, packageName);
		imports.add("javax.persistence.Column");
		imports.add("javax.persistence.Entity");
		imports.add("javax.persistence.Table");
		imports.add("javax.persistence.Id");
		Template template = basicGenerator.retrieveTemplate(PackageType.MODEL);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator.getAndPrepareWriter(path + File.separator + PackageType.MODEL.toString().toLowerCase()
					+ File.separator + classData.getClassName() + ".java");

			context.clear();
			context.put("class", classData);
			context.put("packageName", packageName.concat(".model"));
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

	private void determineEnums(ClassData classData, String packagePath, String packageName) {
		List<Enumeration> allEnums = classData.getEnums();
		if (allEnums != null) {
			allEnums.forEach(enumeration -> {
				enumGeneratorService.generate(enumeration, packagePath, packageName);
				imports.add(packageName.concat(".model.enumeration.") + enumeration.getEnumType());
				imports.add("javax.persistence.Enumerated");
				imports.add("javax.persistence.EnumType");
			});

		}
	}

	private void prepareImports(ClassData classData) {
		if (!classData.getProperties().isEmpty()) {
			imports.add("javax.persistence.ManyToOne");
			imports.add("javax.persistence.FetchType");
			imports.add("javax.persistence.JoinColumn");
			Optional<Property> any = classData.getProperties().stream().filter(propery -> propery.getIsSelfReferenced())
					.findFirst();
			if (any.isPresent()) {
				imports.add("javax.persistence.OneToMany");
				imports.add("javax.persistence.CascadeType");
				imports.add("java.util.Set");
				imports.add("java.util.HashSet");
			}
		}
		Optional<Field> result = classData.getFields().stream().filter(field -> field.getType().equals("Date"))
				.findFirst();
		if (result.isPresent()) {
			imports.add("java.sql.Date");
		}
		Optional<Field> result2 = classData.getFields().stream().filter(field -> field.getType().equals("Blob"))
				.findFirst();
		if (result2.isPresent()) {
			imports.add("java.sql.Blob");
		}
		if(classData.getManyToManyProperty()!= null) {
			imports.add("java.util.ArrayList");
			imports.add("java.util.List");
			imports.add("javax.persistence.ManyToMany");
			imports.add("javax.persistence.JoinTable");
			imports.add("javax.persistence.JoinColumn");
			imports.add("javax.persistence.CascadeType");
		}
	}

	private void handleManyToManyInfo(List<ClassData> allClasses, ClassData m2mClass) {
		Property propertyOne = m2mClass.getProperties().get(0);
		Property propertyTwo = m2mClass.getProperties().get(1);
		allClasses.forEach(classData -> {
			if (propertyOne.getPkTableName().equals(classData.getTableName())) {
				classData.setManyToManyProperty(propertyTwo);
				Optional<Field> pkField = classData.getFields().stream().filter(field -> field.getIsPrimaryKey())
						.findFirst();
				if (pkField.isPresent())
					classData.getManyToManyProperty().setColumnName(pkField.get().getColumnName());
			}
			if (propertyTwo.getPkTableName().equals(classData.getTableName())) {
				classData.setManyToManyProperty(propertyOne);
				Optional<Field> pkField = classData.getFields().stream().filter(field -> field.getIsPrimaryKey())
						.findFirst();
				if (pkField.isPresent())
					classData.getManyToManyProperty().setColumnName(pkField.get().getColumnName());
			}
		});
		
	}

	private FMForeignKey determineCascadeOperation(FMForeignKey foreignKey) {
		final String update = foreignKey.getUpdateRule();
		final String delete = foreignKey.getDeleteRule();
		if (update.equals("importedKeyCascade") && delete.equals("importedKeyCascade")) {
			foreignKey.setCascadeType("ALL");
			imports.add("javax.persistence.CascadeType");
		}
		if (!update.equals("importedKeyCascade") && delete.equals("importedKeyCascade")) {
			foreignKey.setOrphanRemoval(true);
			foreignKey.setCascadeType("REMOVE");
			imports.add("javax.persistence.CascadeType");
		}

		return foreignKey;
	}

}
