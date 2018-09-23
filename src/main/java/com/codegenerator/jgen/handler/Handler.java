package com.codegenerator.jgen.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.model.FMForeignKey;
import com.codegenerator.jgen.database.model.FMTable;
import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.DatabaseConnection;
import com.codegenerator.jgen.handler.model.Enumeration;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.Project;
import com.codegenerator.jgen.handler.model.Property;
import com.codegenerator.jgen.handler.model.Visibility;

@Component
public class Handler {

	public Project metadataToObjects(FMDatabaseMetadata metadata) {
		Project project = new Project();

		// start filling in the basic data
		// Step one: basic info

		// Step two: database connection
		DatabaseConnection dbc = DatabaseConnection.builder().driverName(metadata.getDriverName())
				.url(metadata.getUrl()).username(metadata.getUsername().split("@")[0]).password("").build();
		project.setDatabaseConnection(dbc);

		// Step three: clazezz
		List<ClassData> classes = new ArrayList<>();
		metadata.getTables().forEach(table -> classes.add(createClassFromTable(table)));
		project.setClasses(classes);
		
		return project;
	}

	public ClassData createClassFromTable(FMTable table) {
		ClassData classData = new ClassData();
		classData.setClassName(ClassNamesUtil.toClassName(table.getTableName()));
		classData.setTableName(table.getTableName());

		// get all regular columns aka Fields
		List<FMColumn> fieldColumns = table.getTableColumns().stream()
				.filter(column -> (!column.getIsEnum() && column.getForeignKeyInfo() == null))
				.collect(Collectors.toList());
		List<Field> fields = new ArrayList<>();
		fieldColumns.forEach(column -> {
			Field field = Field.builder().columnName(column.getColumnName())
					.fieldName(ClassNamesUtil.toFieldName(column.getColumnName()))
					.type(determineDataType(column.getColumnTypeName())).visibility(Visibility.PRIVATE)
					.size(column.getColumnSize()).isNullable(column.getIsNullable())
					.isPrimaryKey(column.getIsPrimaryKey()).build();
			fields.add(field);
		});
		classData.setFields(fields);

		// get all the enums
		List<FMColumn> enumColumns = table.getTableColumns().stream().filter(column -> column.getIsEnum())
				.collect(Collectors.toList());
		List<Enumeration> enums = new ArrayList<>();
		enumColumns.forEach(column -> {
			Enumeration enumeration = Enumeration.builder().enumType(ClassNamesUtil.toClassName(column.getColumnName())).visibility(Visibility.PRIVATE).values(column.getEnumValues()).build();
			enums.add(enumeration);
		});
		classData.setEnums(enums);

		// get all the foreign keys
		List<FMColumn> foreignKeyColumns = table.getTableColumns().stream()
				.filter(column -> (column.getForeignKeyInfo() != null)).collect(Collectors.toList());
		List<Property> properties = new ArrayList<>();
		foreignKeyColumns.forEach(column -> {
			FMForeignKey foreignKey = column.getForeignKeyInfo();
			Property property = Property.builder().visibility(Visibility.PRIVATE)
					.classTypeName(ClassNamesUtil.toClassName(foreignKey.getPkTableName()))
					.propertyName(ClassNamesUtil.toFieldName(foreignKey.getPkTableName())).build();
			properties.add(property);
		});
		classData.setProperties(properties);

		return classData;
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
			return "Date";
		default:
			return "Unknown data type";
		}
	}

}
