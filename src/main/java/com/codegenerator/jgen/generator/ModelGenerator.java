package com.codegenerator.jgen.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codegenerator.jgen.model.FMDatabaseMetadata;
import com.codegenerator.jgen.model.FMTable;
import com.codegenerator.jgen.model.PackageType;
import com.codegenerator.jgen.service.GeneratorService;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class ModelGenerator {

	@Autowired
	public GeneratorService generatorService;

	public void generate(FMDatabaseMetadata databaseMetadata) {
		databaseMetadata.getTables().forEach(table -> {
			generateModelClass(table);
		});
	}

	private void generateModelClass(FMTable table) {
		Template template = generatorService.retrieveTemplate(PackageType.MODEL);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = generatorService.getAndPrepareWriter(PackageType.MODEL, table.getTableName());
			context.clear();
			context.put("class", table);
			context.put("fields", table.getTableColumns());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
