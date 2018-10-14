package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.PackageType;
import com.codegenerator.jgen.handler.model.Enumeration;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EnumGeneratorService {

	@Autowired
	public BasicGenerator basicGenerator;
	
	public void generate(Enumeration enumeration, String packagePath, String packageName) {
		generateEnum(enumeration, packagePath, packageName);
	}
	
	private void generateEnum(Enumeration enumeration, String packagePath, String packageName) {
		Template template = basicGenerator.retrieveTemplate(PackageType.ENUMERATION);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = basicGenerator.getAndPrepareWriter(packagePath + File.separator + PackageType.MODEL.toString().toLowerCase()  + File.separator + PackageType.ENUMERATION.toString().toLowerCase()  + File.separator + enumeration.getEnumType() + ".java");
			context.clear();
			context.put("enum", enumeration);
			context.put("packageName", packageName.concat(".model.enumeration"));
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
	}
}
