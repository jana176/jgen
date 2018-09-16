package com.codegenerator.jgen.generator.service;

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

	public void generate(FMDatabaseMetadata databaseMetadata) {
		databaseMetadata.getTables().forEach(table -> generateRepositoryForModelClass(table));
	}
	
	
	private void generateRepositoryForModelClass(FMTable table) {
		final String idType = retrieveIdColumnType(table);
		
		Template template = generatorService.retrieveTemplate(PackageType.REPOSITORY);
		imports.add(String.format("generated.model.%s", table.getClassName()));
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = generatorService.getAndPrepareWriter(PackageType.REPOSITORY, table.getClassName().concat("Repository"));
			context.clear();
			context.put("repoClassName", table.getClassName());
			context.put("idType", idType);
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
