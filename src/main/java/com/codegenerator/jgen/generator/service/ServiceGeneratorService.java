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

import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.enumeration.PackageType;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Relationship;
import com.codegenerator.jgen.handler.model.enumeration.RelationshipType;
import com.codegenerator.jgen.handler.util.ClassNamesUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ServiceGeneratorService extends BasicGenerator {

	private List<String> imports = new ArrayList<>();

	public void generate(List<ClassData> classes, String path, String packageName) {
		List<ClassData> classesToGenerateServiceFor = classes.stream()
				.filter(classData -> classData.getService().getGenerateService()
						&& generateService(classData.getRelationship()))
				.collect(Collectors.toList());

		classesToGenerateServiceFor.forEach(classData -> {
			generateServiceForModelClass(classData, path, packageName);
		});
	}

	private void generateServiceForModelClass(ClassData classData, String path, String packageName) {
		final String idType = retrieveIdColumnType(classData, packageName);

		imports.add(packageName + ".model." + classData.getClassName());
		imports.add(packageName + ".repository." + classData.getClassName() + "Repository");

		Template template = retrieveTemplate(PackageType.SERVICE);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getAndPrepareWriter(path + File.separator + PackageType.SERVICE.toString().toLowerCase()
					+ File.separator + classData.getClassName().concat("Service") + ".java");
			context.clear();
			context.put("class", classData);
			context.put("fieldName", ClassNamesUtil.toFieldName(classData.getClassName()));
			context.put("idType", idType);
			context.put("packageName", packageName.concat(".service"));
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

	private Boolean generateService(Relationship relationship) {
		if ((relationship.getRelationshipType() != null
				&& relationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY_SEPARATE_CLASS))
				|| relationship.getRelationshipType() == null) {
			return true;
		} else {
			return false;
		}
	}
}
