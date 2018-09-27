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
import com.codegenerator.jgen.generator.model.NewProjectInfo;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.ControllerOperations;
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
		//@formatter:off
		DatabaseConnection dbc = DatabaseConnection.builder()
				.driverName(metadata.getDriverName())
				.url(metadata.getUrl())
				.username(metadata.getUsername().split("@")[0])
				.password("")
				.build();
		//@formatter:on
		project.setDatabaseConnection(dbc);

		// Step three: clazezz
		List<ClassData> classes = new ArrayList<>();
		metadata.getTables().forEach(table -> classes.add(createClassFromTable(table)));
		project.setClasses(classes);

		NewProjectInfo npi = new NewProjectInfo();
		project.setNewProjectInfo(npi);

		return project;
	}

	public ClassData createClassFromTable(FMTable table) {
		//@formatter:off
		ClassData classData = ClassData.builder()
				.className(ClassNamesUtil.toClassName(table.getTableName()))
				.tableName(table.getTableName())
				.relationshipClass(false)
				.build();
		//@formatter:on
		
		// get all regular columns aka Fields
		List<FMColumn> fieldColumns = table.getTableColumns().stream()
				.filter(column -> (!column.getIsEnum() && column.getForeignKeyInfo() == null))
				.collect(Collectors.toList());
		List<Field> fields = new ArrayList<>();
		fieldColumns.forEach(column -> {
			//@formatter:off
			Field field = Field.builder().columnName(column.getColumnName())
					.fieldName(ClassNamesUtil.toFieldName(column.getColumnName()))
					.type(determineDataType(column.getColumnTypeName()))
					.visibility(Visibility.PRIVATE)
					.size(column.getColumnSize())
					.precision(column.getDecimalDigits())
					.isNullable(column.getIsNullable())
					.isPrimaryKey(column.getIsPrimaryKey())
					.isGenerated(column.getIsGenerated())
					.isUnique(column.getIsUnique())
					.build();
			//@formatter:on
			fields.add(field);
		});
		classData.setFields(fields);

		// get all the enums
		List<FMColumn> enumColumns = table.getTableColumns().stream().filter(column -> column.getIsEnum())
				.collect(Collectors.toList());
		List<Enumeration> enums = new ArrayList<>();
		enumColumns.forEach(column -> {
			//@formatter:off
			Enumeration enumeration = Enumeration.builder()
					.enumType(ClassNamesUtil.toClassName(column.getColumnName()))
					.visibility(Visibility.PRIVATE)
					.values(column.getEnumValues())
					.build();
			//@formatter:on
			enums.add(enumeration);
		});
		classData.setEnums(enums);

		// get all the foreign keys
		List<FMColumn> foreignKeyColumns = table.getTableColumns().stream()
				.filter(column -> (column.getForeignKeyInfo() != null)).collect(Collectors.toList());
		List<Property> properties = new ArrayList<>();
		foreignKeyColumns.forEach(column -> {
			FMForeignKey foreignKey = column.getForeignKeyInfo();
			//@formatter:off
			Property property = Property.builder().visibility(Visibility.PRIVATE)
					.pkTableName(foreignKey.getPkTableName())
					.columnName(foreignKey.getFkColumnName())
					.pkClassName(ClassNamesUtil.toClassName(foreignKey.getPkTableName()))
					.propertyName(ClassNamesUtil.toFieldName(foreignKey.getFkColumnName()))
					.build();
			if(column.getTableName().toLowerCase().equals(foreignKey.getPkTableName().toLowerCase())){
				property.setIsSelfReferenced(true);
			}
			properties.add(property);
			//@formatter:on
		});
		classData.setProperties(properties);

		ControllerOperations co = ControllerOperations.builder().build();
		classData.setControllerOperations(co);
		
		return classData;
	}

	private String determineDataType(String dbDataType) {

		switch (dbDataType) {
		case "BIT":
		case "BOOLEAN":
			return "Boolean";
		case "CHAR":
		case "VARCHAR":
		case "TEXT":
			return "String";
		case "DOUBLE":
		case "FLOAT":
		case "DECIMAL":
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
		case "MEDIUMBLOB":
		case "MEDIUMTEXT":
			return "Blob";
		default:
			return "Unknown data type";
		}
	}

}
