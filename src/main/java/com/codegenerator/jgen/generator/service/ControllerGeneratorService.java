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

import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.generator.model.PackageType;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Project;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ControllerGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;

	private List<String> imports = new ArrayList<>();

	public void generate(Project project, String path, String packageName) {
		List<ClassData> classesToGenerateControllerFor = project.getClasses().stream()
				.filter(classData -> classData.getController().getGenerateController() && !classData.getRelationship().getIsRelationshipClass())
				.collect(Collectors.toList());

		classesToGenerateControllerFor.forEach(classData -> {
			generateControllerForModelClass(classData, path, packageName);
		});
	}

	private void generateControllerForModelClass(ClassData classData, String path, String packageName) {
		prepareImports(classData);
		final Field idField = retrieveIdColumn(classData);
		imports.add(packageName + ".model." + classData.getClassName());
		imports.add(packageName + ".service." + classData.getClassName() + "Service");

		Template template = basicGenerator.retrieveTemplate(PackageType.CONTROLLER);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator
					.getAndPrepareWriter(path + File.separator + PackageType.CONTROLLER.toString().toLowerCase()
							+ File.separator + classData.getClassName().concat("Controller") + ".java");
			context.clear();
			context.put("class", classData);
			context.put("fieldName", ClassNamesUtil.toFieldName(classData.getClassName()));
			context.put("idField", idField);
			context.put("packageName", packageName.concat(".controller"));
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

	private void prepareImports(ClassData classData) {
		if (classData.getController().getControllerOperations().getGet()) {
			imports.add("org.springframework.web.bind.annotation.GetMapping");

		}
		if (classData.getController().getControllerOperations().getPut()) {
			imports.add("org.springframework.web.bind.annotation.PutMapping");
			imports.add("org.springframework.http.ResponseEntity");
		}
		if (classData.getController().getControllerOperations().getPost()) {
			imports.add("org.springframework.web.bind.annotation.PostMapping");
			imports.add("org.springframework.web.bind.annotation.RequestBody");
		}
		if (classData.getController().getControllerOperations().getDelete()) {
			imports.add("org.springframework.web.bind.annotation.DeleteMapping");
		}
	}

	private Field retrieveIdColumn(ClassData classData) {
		final Optional<Field> idColumn = classData.getFields().stream().filter(field -> field.getIsPrimaryKey())
				.findAny();
		if (idColumn.isPresent()) {
			String idFieldName = idColumn.get().getFieldName();
			String cap = idFieldName.substring(0, 1).toUpperCase() + idFieldName.substring(1);
			idColumn.get().setFieldName(cap);
			return idColumn.get();
		} else
			return null;
	}
}
