package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codegenerator.jgen.generator.BasicGenerator;
import com.codegenerator.jgen.generator.model.enumeration.PackageType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class NamingConventionGeneratorService extends BasicGenerator {

	public void generate(String path, String packageName) {
		generateCustomNamingStrategyClass(path, packageName);
	}

	private void generateCustomNamingStrategyClass(final String path, String packageName) {
		Template template = retrieveTemplate(PackageType.NAMING_STRATEGY);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		File outputFile = new File(path + File.separator + "RealNamingStrategyImpl.java");
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.put("packageName", packageName);
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
