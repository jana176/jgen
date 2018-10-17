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

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class CompositeKeyModelGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;

	private List<String> imports = new ArrayList<>();

	public void generate(ClassData classData, List<String> compositePks, String path, String packageName) {

		generateCompositeKeyModelClass(classData, path, packageName);

	}

	private void generateCompositeKeyModelClass(ClassData classData, String path, String packageName) {
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
			context.put("compositeKey", classData.getCompositeKey());
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

}
