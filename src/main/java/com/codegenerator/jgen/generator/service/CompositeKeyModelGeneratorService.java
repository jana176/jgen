package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.enumeration.PackageType;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.CompositeKey;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Property;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class CompositeKeyModelGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;

	private List<String> imports = new ArrayList<>();

	public void generate(ClassData classData, List<String> compositePks, String path, String packageName) {

		CompositeKey compositeKey = createCompositeKey(classData, compositePks);
		classData.setCompositeKey(compositeKey);
		generateCompositeKeyModelClass(classData, compositeKey, path, packageName);

	}

	private void generateCompositeKeyModelClass(ClassData classData, CompositeKey compositeKey, String path,
			String packageName) {
		imports.add("javax.persistence.Embeddable");
		imports.add("javax.persistence.Column");
		imports.add("java.io.Serializable");
		Template template = basicGenerator.retrieveTemplate(PackageType.EMBEDDED_KEY);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator.getAndPrepareWriter(path + File.separator + PackageType.MODEL.toString().toLowerCase()
					+ File.separator + classData.getClassName() + "Id.java");
			context.clear();
			context.put("class", classData);
			context.put("compositeKey", compositeKey);
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

	private CompositeKey createCompositeKey(ClassData classData, List<String> compositePks) {
		CompositeKey compositeKey = new CompositeKey(classData.getTableName(), new ArrayList<Field>(),
				new ArrayList<Property>());
		compositePks.stream().forEach(ck -> {
			classData.getFields().stream().forEach(field -> {
				if (field.getColumnName().equals(ck)) {
					compositeKey.getFields().add(field);
				}
			});
			classData.getProperties().stream().forEach(property -> {
				if (property.getColumnName().equals(ck)) {
					compositeKey.getProperties().add(property);
				}
			});
		});
		return compositeKey;
	}

}
