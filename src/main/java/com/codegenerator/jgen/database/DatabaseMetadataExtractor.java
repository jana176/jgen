package com.codegenerator.jgen.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.codegenerator.jgen.database.model.FMColumn;
import com.codegenerator.jgen.database.model.FMDatabaseMetadata;
import com.codegenerator.jgen.database.model.FMForeignKey;
import com.codegenerator.jgen.database.model.FMTable;
import com.codegenerator.jgen.handler.util.ClassNamesUtil;

@Repository
public class DatabaseMetadataExtractor {

	@Autowired
	public Connection connection;

	public FMDatabaseMetadata getDatabaseMetadata() {
		FMDatabaseMetadata databaseMetadata = new FMDatabaseMetadata();

		try {
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet tableMetaData = metadata.getTables(null, null, "%", new String[] { "TABLE" });

			List<FMTable> tables = processTables(tableMetaData);
			processColumnsForTable(tables, metadata);
			processForeignKeysForTable(tables, metadata);
			processIndexedColumnsForTable(tables, metadata);
			processPrimaryKeysForTable(tables, metadata);

			databaseMetadata.setDriverName("com.mysql.jdbc.Driver");
			databaseMetadata.setUrl(metadata.getURL());
			databaseMetadata.setUsername(metadata.getUserName().split("@")[0]);
			databaseMetadata.setTables(tables);

		} catch (Exception e) {
			System.out.println("Error while trying to extract database metadata: " + e);
		}

		return databaseMetadata;
	}

	private List<FMTable> processTables(ResultSet resultSet) throws SQLException {
		List<FMTable> tables = new ArrayList<>();
		while (resultSet.next()) {
			FMTable table = new FMTable();
			table.setTableSchema(resultSet.getString("TABLE_CAT"));
			table.setTableName(resultSet.getString("TABLE_NAME"));
			table.setTableType(resultSet.getString("TABLE_TYPE"));
			tables.add(table);
		}
		return tables;
	}

	private void processColumnsForTable(List<FMTable> tables, DatabaseMetaData metadata) {

		tables.forEach(table -> {
			String name = table.getTableName();
			ResultSet columnsResultSet;
			try {
				columnsResultSet = metadata.getColumns(null, null, name, null);
				List<FMColumn> columns = new ArrayList<>();
				while (columnsResultSet.next()) {
					FMColumn column = new FMColumn();
					column.setTableName(name);
					column.setColumnName(columnsResultSet.getString("COLUMN_NAME"));
					column.setColumnTypeName(columnsResultSet.getString("TYPE_NAME"));
					if (column.getColumnTypeName().equals("ENUM")) {
						column.setIsEnum(true);
						retrieveEnumValues(column, connection);
					}
					column.setColumnSize(columnsResultSet.getInt("COLUMN_SIZE"));
					column.setColumnDefault(columnsResultSet.getString("COLUMN_DEF"));
					column.setDecimalDigits(columnsResultSet.getInt("DECIMAL_DIGITS"));
					column.setIsNullable(columnsResultSet.getBoolean("IS_NULLABLE"));
					column.setIsAutoincrement(columnsResultSet.getBoolean("IS_AUTOINCREMENT"));
					column.setIsGenerated(columnsResultSet.getBoolean("IS_GENERATEDCOLUMN"));
					columns.add(column);
				}
				table.setTableColumns(columns);
			} catch (SQLException e) {
				System.out.println("Error while trying to fetch columns for tables: " + e);
			}
		});
	}

	private void retrieveEnumValues(FMColumn column, Connection connection) {
		String query = String.format("SHOW COLUMNS FROM %s LIKE '%s'", column.getTableName(), column.getColumnName());
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				column.setEnumValues(ClassNamesUtil.separateEnumValues(rs.getString("Type")));
			}
		} catch (SQLException e) {
			System.out.println("Error while trying to fetch enumerated values for columns: " + e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Error while trying to close executed statement: " + e);
				}
			}
		}
	}

	private void processForeignKeysForTable(List<FMTable> tables, DatabaseMetaData metadata) {
		tables.stream().forEach(table -> {
			ResultSet resultSet;
			try {
				resultSet = metadata.getImportedKeys(null, null, table.getTableName());
				List<FMForeignKey> foreignKeys = new ArrayList<>();
				while (resultSet.next()) {
					FMForeignKey foreignKey = new FMForeignKey();
					foreignKey.setPkTableSchema(resultSet.getString("PKTABLE_CAT"));
					foreignKey.setPkTableName(resultSet.getString("PKTABLE_NAME"));
					foreignKey.setPkColumnName(resultSet.getString("PKCOLUMN_NAME"));
					foreignKey.setFkTableSchema(resultSet.getString("FKTABLE_CAT"));
					foreignKey.setFkTableName(resultSet.getString("FKTABLE_NAME"));
					foreignKey.setFkColumnName(resultSet.getString("FKCOLUMN_NAME"));
					foreignKey.setUpdateRule(determineUpdateRule(resultSet.getString("UPDATE_RULE")));
					foreignKey.setDeleteRule(determineDeleteRule(resultSet.getString("DELETE_RULE")));

					table.getTableColumns().stream().forEach(column -> {
						if (column.getColumnName().toUpperCase().equals(foreignKey.getFkColumnName().toUpperCase())) {
							column.setForeignKeyInfo(foreignKey);
						}
					});
					foreignKeys.add(foreignKey);
				}
			} catch (SQLException e) {
				System.out.println("Error while trying to fetch foreign keys for tables: " + e);
			}
		});
	}
	
	private void processPrimaryKeysForTable(List<FMTable> tables, DatabaseMetaData metadata) {
		tables.stream().forEach(table -> {
			table.setCompositePrimaryKeyColumns(new ArrayList<>());
			List<String> primaryKeysPerTable = new ArrayList<>();
			ResultSet resultSet;
			try {
				resultSet = metadata.getPrimaryKeys(null, null, table.getTableName());
				while (resultSet.next()) {
					String primaryKeyColumn = resultSet.getString("COLUMN_NAME");
					table.getTableColumns().stream().forEach(column -> {
						if (column.getColumnName().equals(primaryKeyColumn)) {
							column.setIsPrimaryKey(true);
							column.setIsUnique(true);
							primaryKeysPerTable.add(primaryKeyColumn);
						}
					});
				}
			} catch (SQLException e) {
				System.out.println("Error while trying to fetch primary key for table: " + e);
			}
			if (primaryKeysPerTable.size() >= 2)
				table.getCompositePrimaryKeyColumns().addAll(primaryKeysPerTable);
			primaryKeysPerTable.clear();
		});		
	}

	private void processIndexedColumnsForTable(List<FMTable> tables, DatabaseMetaData metadata) {
		tables.stream().forEach(table -> {
			try {
				ResultSet resultSet = metadata.getIndexInfo(null, null, table.getTableName(), true, false);
				List<String> uniqueColumns = new ArrayList<>();
				while (resultSet.next()) {
					String uniqueColumn = resultSet.getString("COLUMN_NAME");
					uniqueColumns.add(uniqueColumn);
					table.getTableColumns().stream().forEach(column -> {
						if (column.getColumnName().equals(uniqueColumn)) {
							column.setIsUnique(true);
						}
					});
				}
				table.setUniqueColumns(uniqueColumns);
			} catch (SQLException e) {
				System.out.println("Error while trying to fetch indexes for tables: " + e);
			}
		});
	}

	private String determineUpdateRule(String index) {
		switch (index) {
		case "0":
			return "importedNoAction";
		case "1":
			return "importedKeyCascade";
		case "2":
			return "importedKeySetNull";
		case "3":
			return "importedKeySetDefault";
		case "4":
			return "importedKeyRestrict";
		default:
			return "";
		}
	}

	private String determineDeleteRule(String index) {
		switch (index) {
		case "0":
			return "importedKeyNoAction";
		case "1":
			return "importedKeyCascade";
		case "2":
			return "importedKeySetNull";
		case "3":
			return "importedKeyRestrict";
		case "4":
			return "importedKeySetDefault";
		default:
			return "";
		}
	}

}
