package com.codegenerator.jgen.generator.service;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.model.FMForeignKey;
import com.codegenerator.jgen.database.model.FMTable;
import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.model.PackageType;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ModelGeneratorService {

	@Autowired
	public GeneratorService generatorService;

	@Autowired
	public EnumGeneratorService enumGeneratorService;

	private List<String> imports = new ArrayList<>();

	public void generate(FMDatabaseMetadata databaseMetadata) {
		databaseMetadata.getTables().forEach(table -> {
			table.getTableColumns().forEach(column -> preprocessColumn(column));
			generateModelClass(table);
		});
	}

	private void generateModelClass(FMTable table) {
		prepareTableData(table);
		Template template = generatorService.retrieveTemplate(PackageType.MODEL);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = generatorService.getAndPrepareWriter(PackageType.MODEL, ClassNamesUtil.toClassName(table.getTableName()));
			context.clear();
			context.put("class", table);
			context.put("fields", table.getTableColumns());
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

	private void preprocessColumn(FMColumn column) {
		if (column.getIsEnum()) {
			enumGeneratorService.generate(column);
			imports.add("generated.model.enumeration." + ClassNamesUtil.toClassName(column.getColumnName()));
		}
		if (column.getForeignKeyInfo() != null) {
			FMForeignKey foreignKey = column.getForeignKeyInfo();
			foreignKey.setPkTableName(ClassNamesUtil.toClassName(foreignKey.getPkTableName()));
			foreignKey.setPkColumnName(ClassNamesUtil.toFieldName(foreignKey.getPkTableName()));
			determineCascadeOperation(foreignKey);
			imports.add("javax.persistence.ManyToOne");
		}
	}

	private FMForeignKey determineCascadeOperation(FMForeignKey foreignKey) {
		final String update = foreignKey.getUpdateRule();
		final String delete = foreignKey.getDeleteRule();
		if (update.equals("importedKeyCascade") && delete.equals("importedKeyCascade")) {
			foreignKey.setCascadeType("ALL");
			imports.add("javax.persistence.CascadeType");
		}
		if (!update.equals("importedKeyCascade") && delete.equals("importedKeyCascade")) {
			foreignKey.setOrphanRemoval(true);
			foreignKey.setCascadeType("REMOVE");
			imports.add("javax.persistence.CascadeType");
		}
		
		return foreignKey;
	}

	private FMTable prepareTableData(FMTable table) {
		imports.add("javax.persistence.Column");
		imports.add("javax.persistence.Entity");
		imports.add("javax.persistence.Table");
		imports.add("javax.persistence.Id");
		table.getTableColumns().forEach(column -> {
			if (!column.getIsEnum()) {
				column.setColumnTypeName(determineDataType(column.getColumnTypeName()));
			} else {
				imports.add("javax.persistence.Enumerated");
				imports.add("javax.persistence.EnumType");
				column.setColumnTypeName(ClassNamesUtil.toClassName(column.getColumnName()));
			}

			column.setFieldName(ClassNamesUtil.toFieldName(column.getColumnName()));
		});

		return table;
	}

	private String determineDataType(String dbDataType) {

		switch (dbDataType) {
		case "BIT":
		case "BOOLEAN":
			return "Boolean";
		case "CHAR":
		case "VARCHAR":
			return "String";
		case "DOUBLE":
		case "FLOAT":
			return "Double";
		case "INTEGER":
		case "INT":
		case "NUMERIC":
		case "SMALLINT":
			return "Integer";
		case "BIGINT":
			return "Long";
		case "TIMESTAMP":
		case "DATE":
			imports.add("java.sql.Date");
			return "Date";
		default:
			return "Unknown data type";
		}
	}

}
