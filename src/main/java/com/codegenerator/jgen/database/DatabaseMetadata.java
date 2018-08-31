package com.codegenerator.jgen.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.codegenerator.jgen.model.FMForeignKey;
import com.codegenerator.jgen.model.FMTable;

@Component
public class DatabaseMetadata {

	public void getDatabaseMetadata(Connection connection) {

		try {
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet tableMetaData = metadata.getTables(null, null, "%", new String[] { "TABLE" });
			List<FMTable> tables = processTables(tableMetaData);

			tables.stream().forEach(table -> System.out.println(table.toString()));

			// FOR EVERY TABLE
			tables.stream().forEach(table -> {
				try {
					String name = table.getTableName().toString();
					// GET COLUMNS FOR A TABLE
					ResultSet columns = metadata.getColumns(null, null, name.toString(), null);
					System.out.println("Table: " + name);
					while (columns.next()) {
						String columnName = columns.getString("COLUMN_NAME");
						String datatype = columns.getString("TYPE_NAME");
						String columnsize = columns.getString("COLUMN_SIZE");
						String decimaldigits = columns.getString("DECIMAL_DIGITS");
						String isNullable = columns.getString("IS_NULLABLE");
						String is_autoIncrment = columns.getString("IS_AUTOINCREMENT");
						// Printing results
						System.out.println(columnName + "---" + datatype + "---" + columnsize + "---" + decimaldigits
								+ "---" + isNullable + "---" + is_autoIncrment);
					}

					// GET PRIMARY KEYS
					ResultSet PK = metadata.getPrimaryKeys(null, null, name.toString());
					System.out.println("------------PRIMARY KEYS-------------");
					while (PK.next()) {
						System.out.println(PK.getString("COLUMN_NAME") + "===" + PK.getString("PK_NAME"));
					}

					// GET FOREIGN KEYS
					ResultSet FK = metadata.getImportedKeys(null, null, name.toString());
					System.out.println("------------FOREIGN KEYS-------------");
					processForeignKeys(FK);

					// GET UNIQUE COLUMNS
					ResultSet unique = metadata.getIndexInfo(null, null, name.toString(), true, false);
					System.out.println("------------UNIQUE COLUMNS-------------");
					while (unique.next()) {
						System.out.println(unique.getString("COLUMN_NAME"));
					}

				} catch (Exception e) {
					System.out.println(e);
				}

			});

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private List<FMTable> processTables(ResultSet resultSet) throws SQLException {
		List<FMTable> tables = new ArrayList<>();

		while (resultSet.next()) {
			FMTable table = new FMTable();
			table.setTableName(resultSet.getString(3));
			table.setTableTypeName(resultSet.getString(4));
			table.setTableCatalog(resultSet.getString(6));
			table.setTableSchema(resultSet.getString(7));

			tables.add(table);
		}
		return tables;
	}

	private List<FMForeignKey> processForeignKeys(ResultSet resultSet) throws SQLException {

		while (resultSet.next()) {
			System.out.println(resultSet.getString("PKTABLE_NAME") + "---" + resultSet.getString("PKCOLUMN_NAME")
					+ "===" + resultSet.getString("FKTABLE_NAME") + "---" + resultSet.getString("FKCOLUMN_NAME"));
		}
		List<FMForeignKey> foreignKeys = new ArrayList<>();
		return foreignKeys;
	}

}
