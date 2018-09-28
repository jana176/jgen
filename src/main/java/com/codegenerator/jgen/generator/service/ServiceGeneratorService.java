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
public class ServiceGeneratorService {


	@Autowired
	public BasicGenerator basicGenerator;
	
	private List<String> imports = new ArrayList<>();
	
	public void generate(Project project, String path, String packageName) {
		List<ClassData> classesToGenerateServiceFor =  project.getClasses().stream().filter(classData -> classData.getService().getGenerateService() && !classData.getRelationshipClass()).collect(Collectors.toList());
		
		classesToGenerateServiceFor.forEach(classData -> {
			generateServiceForModelClass(classData, path, packageName);
		});
	}
	
	private void generateServiceForModelClass(ClassData classData, String path, String packageName) {
		final String idType = retrieveIdColumnType(classData);

		imports.add(packageName + ".model." + classData.getClassName());
		imports.add(packageName + ".repository." + classData.getClassName() + "Repository");
		
		Template template = basicGenerator.retrieveTemplate(PackageType.SERVICE);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator.getAndPrepareWriter(path + File.separator + PackageType.SERVICE.toString().toLowerCase() + File.separator + classData.getClassName().concat("Service") + ".java");
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
	
	private String retrieveIdColumnType(ClassData classData) {
		final Optional<Field> idColumn = classData.getFields().stream().filter(field -> field.getIsPrimaryKey()).findAny();
		return idColumn.get().getType();
	}
}
