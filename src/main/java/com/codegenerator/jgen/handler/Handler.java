package com.codegenerator.jgen.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.model.FMForeignKey;
import com.codegenerator.jgen.database.model.FMTable;
import com.codegenerator.jgen.generator.ClassNamesUtil;
import com.codegenerator.jgen.generator.model.GenerateClassesRequest;
import com.codegenerator.jgen.generator.model.GenerateProjectRequest;
import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Controller;
import com.codegenerator.jgen.handler.model.ControllerOperations;
import com.codegenerator.jgen.handler.model.DatabaseConnection;
import com.codegenerator.jgen.handler.model.Enumeration;
import com.codegenerator.jgen.handler.model.Field;
import com.codegenerator.jgen.handler.model.NewProjectInfo;
import com.codegenerator.jgen.handler.model.Property;
import com.codegenerator.jgen.handler.model.Relationship;
import com.codegenerator.jgen.handler.model.ServiceOperations;
import com.codegenerator.jgen.handler.model.enumeration.RelationshipType;
import com.codegenerator.jgen.handler.model.enumeration.Visibility;

@Component
public class Handler {

	public GenerateClassesRequest metadataToClassesObjects(FMDatabaseMetadata metadata) {
		GenerateClassesRequest generateClassesRequest = new GenerateClassesRequest();

		List<ClassData> classes = new ArrayList<>();
		metadata.getTables().forEach(table -> classes.add(createClassFromTable(table)));
		generateClassesRequest.setClasses(classes);
		generateClassesRequest.setPath(null);
		determineRelationTables(classes);
		return generateClassesRequest;
	}

	public GenerateProjectRequest metadataToProjectObjects(FMDatabaseMetadata metadata) {
		GenerateProjectRequest generateProjectRequest = new GenerateProjectRequest();

		//@formatter:off
			DatabaseConnection dbc = DatabaseConnection.builder()
					.driverName(metadata.getDriverName())
					.url(metadata.getUrl())
					.username(metadata.getUsername().split("@")[0])
					.password(null)
					.build();
			//@formatter:on
		generateProjectRequest.setDatabaseConnection(dbc);

		List<ClassData> classes = new ArrayList<>();
		metadata.getTables().forEach(table -> classes.add(createClassFromTable(table)));
		generateProjectRequest.setClasses(classes);
		determineRelationTables(classes);
		NewProjectInfo npi = new NewProjectInfo();
		generateProjectRequest.setNewProjectInfo(npi);
		
		return generateProjectRequest;
	}

	public ClassData createClassFromTable(FMTable table) {
		Relationship relation = Relationship.builder().isRelationshipClass(false).relationshipType(null).build();
		//@formatter:off
		ClassData classData = ClassData.builder()
				.className(ClassNamesUtil.toClassName(table.getTableName()))
				.tableName(table.getTableName())
				.relationship(relation)
				.manyToManyProperty(null)
				.compositePks(new ArrayList<>())
				.compositeKey(null)
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
					.pkClassName(ClassNamesUtil.toClassName(foreignKey.getPkTableName()))
					.pkColumnName(foreignKey.getPkColumnName())
					.columnName(foreignKey.getFkColumnName())
					.fieldName(ClassNamesUtil.toFieldName(foreignKey.getFkColumnName()))
					.fetch("LAZY")
					.orphanRemoval(false)
					.isSelfReferenced(false)
					.type(determineDataType(column.getColumnTypeName()))
					.visibility(Visibility.PRIVATE)
					.size(column.getColumnSize())
					.precision(column.getDecimalDigits())
					.isNullable(column.getIsNullable())
					.isPrimaryKey(column.getIsPrimaryKey())
					.isGenerated(column.getIsGenerated())
					.isUnique(column.getIsUnique())
					.build();
			if(column.getTableName().toLowerCase().equals(foreignKey.getPkTableName().toLowerCase())){
				property.setIsSelfReferenced(true);
			}
			properties.add(property);
			//@formatter:on
		});
		classData.setProperties(properties);

		ControllerOperations co = ControllerOperations.builder().build();
		Controller c = Controller.builder().controllerOperations(co).build();
		classData.setController(c);
		ServiceOperations so = ServiceOperations.builder().build();
		com.codegenerator.jgen.handler.model.Service s = com.codegenerator.jgen.handler.model.Service.builder()
				.serviceOperations(so).build();
		classData.setService(s);

		// get all composite PK's
		if (table.getCompositePrimaryKeyColumns() != null) {
			table.getCompositePrimaryKeyColumns().forEach(name -> {
				Optional<Field> pkField = classData.getFields().stream().filter(f -> f.getColumnName().equals(name))
						.findFirst();
				if (pkField.isPresent()) {
					classData.getCompositePks().add(pkField.get().getColumnName());
				}
				Optional<Property> pkProperty = classData.getProperties().stream()
						.filter(p -> p.getColumnName().equals(name)).findFirst();
				if (pkProperty.isPresent()) {
					classData.getCompositePks().add(pkProperty.get().getColumnName());
				}
			});
		}

		return classData;
	}

	private void determineRelationTables(List<ClassData> classes) {
		List<Property> pkProperties = new ArrayList<>();
		List<Property> fkProperties = new ArrayList<>();

		classes.forEach(classData -> {
			classData.getProperties().forEach(property -> {
				if (!property.getIsSelfReferenced()) {
					if (property.getIsPrimaryKey()) {
						pkProperties.add(property);
					} else
						fkProperties.add(property);
				}

			});
			if (pkProperties.size() >= 2) {
//				System.out
//						.println("Ima bar dva strana kljuca kao primarna, poveznicka tabela sa kompozitnim kljucem! - "
//								+ classData.getTableName());
				classData.getRelationship().setIsRelationshipClass(true);
				if (classData.getFields().size() > 0 || classData.getEnums().size() > 0)
					classData.getRelationship().setRelationshipType(RelationshipType.MANY_TO_MANY_SEPARATE_CLASS);
				else
					classData.getRelationship().setRelationshipType(RelationshipType.MANY_TO_MANY);
			}
			if (fkProperties.size() >= 2) {
//				System.out.println("Ima bar dva strana kljuca ali nisu primarni, MOZDA je poveznicka tablela! - "
//						+ classData.getTableName());
			}
			pkProperties.clear();
			fkProperties.clear();
		});
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
		case "SMALLINT UNSIGNED":
		case "TINYINT UNSIGNED":
		case "MEDIUMINT UNSIGNED":
			return "Integer";
		case "BIGINT":
			return "Long";
		case "TIMESTAMP":
		case "DATE":
		case "DATETIME":
		case "YEAR":
			return "Date";
		case "MEDIUMBLOB":
		case "MEDIUMTEXT":
		case "GEOMETRY":
		case "BLOB":
			return "Blob";
		default:
			return "Unknown data type";
		}
	}

}
