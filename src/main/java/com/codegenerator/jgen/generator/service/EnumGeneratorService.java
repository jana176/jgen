package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.model.PackageType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EnumGeneratorService {

	@Autowired
	public GeneratorService generatorService;
	
	public void generate(FMColumn column) {
		generateEnum(column.getColumnName(), column.getEnumValues());
	}
	
	private void generateEnum(String enumName, List<String> enumValues) {
		String className = ClassNamesUtil.toClassName(enumName);
		Template template = generatorService.retrieveTemplate(PackageType.ENUMERATION);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = generatorService.getAndPrepareWriter(PackageType.ENUMERATION, className);
			context.clear();
			context.put("name", className);
			context.put("values", enumValues);
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
