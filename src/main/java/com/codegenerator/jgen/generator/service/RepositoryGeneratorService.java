package com.codegenerator.jgen.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.model.FMTable;
import com.codegenerator.jgen.model.PackageType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class RepositoryGeneratorService {

	@Autowired
	public GeneratorService generatorService;
	
	private List<String> imports = new ArrayList<>();

	public void generate(FMDatabaseMetadata databaseMetadata, String path, String packageName) {
		databaseMetadata.getTables().forEach(table -> generateRepositoryForModelClass(table, path, packageName));
	}
	
	private void generateRepositoryForModelClass(FMTable table, String path, String packageName) {
		final String idType = retrieveIdColumnType(table);
		imports.add(packageName + ".model." + table.getClassName());
		
		Template template = generatorService.retrieveTemplate(PackageType.REPOSITORY);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = generatorService.getAndPrepareWriter(path + File.separator + PackageType.REPOSITORY.toString().toLowerCase() + File.separator + table.getClassName().concat("Repository") + ".java");
			context.clear();
			context.put("repoClassName", table.getClassName());
			context.put("idType", idType);
			context.put("packageName", packageName.concat(".repository"));
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
	
	
	private String retrieveIdColumnType(FMTable table) {
		final Optional<FMColumn> idColumn = table.getTableColumns().stream().filter(column -> column.getIsPrimaryKey()).findAny();
		return idColumn.get().getColumnTypeName();
	}
	
}
