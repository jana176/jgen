package com.codegenerator.jgen.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DatabaseMetadata {

	
	public void getDatabaseMetadata(Connection connection) {

		
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet rsmd = metadata.getTables(null, null, "%", new String[] { "TABLE" });
			
			List<List<String>> resultsetlist = new ArrayList<>();
			ResultSetMetaData metadata2 = rsmd.getMetaData();
			int numberOfColumns = metadata2.getColumnCount();

			List<String> tableNames = new ArrayList<>();

			while (rsmd.next()) {
				int i = 1;
				List<String> thatRowData = new ArrayList<>();
				while (i <= numberOfColumns) {
					thatRowData.add(rsmd.getString(i++));
					if (i == 3) {
						tableNames.add(rsmd.getString(i));
					}
				}
				resultsetlist.add(thatRowData);
			}

			System.out.println(numberOfColumns);
			StringBuilder sb = new StringBuilder();
			resultsetlist.stream().forEach(result -> {
				sb.append(result);
				sb.append("\t");
			});
			System.out.println(sb);

			System.out.println(tableNames.size());

			// FOR EVERY TABLE	
			tableNames.stream().forEach(name -> {
				try {
					
					//GET COLUMNS FOR A TABLE
					ResultSet columns = metadata.getColumns(null,null, name.toString(), null);
					System.out.println("Table: " + name);
					while(columns.next()) {
						String columnName = columns.getString("COLUMN_NAME");
					    String datatype = columns.getString("TYPE_NAME");
					    String columnsize = columns.getString("COLUMN_SIZE");
					    String decimaldigits = columns.getString("DECIMAL_DIGITS");
					    String isNullable = columns.getString("IS_NULLABLE");
					    String is_autoIncrment = columns.getString("IS_AUTOINCREMENT");
					    //Printing results
					    System.out.println(columnName + "---" + datatype + "---" + columnsize + "---" + decimaldigits + "---" + isNullable + "---" + is_autoIncrment);
					}
					
					
					//GET PRIMARY KEYS
					ResultSet PK = metadata.getPrimaryKeys(null,null, name.toString());
					System.out.println("------------PRIMARY KEYS-------------");
					while(PK.next())
					{
					    System.out.println(PK.getString("COLUMN_NAME") + "===" + PK.getString("PK_NAME"));
					}
					
					//GET FOREIGN KEYS
					ResultSet FK = metadata.getImportedKeys(null, null, name.toString());
					System.out.println("------------FOREIGN KEYS-------------");
					while(FK.next())
					{
					    System.out.println(FK.getString("PKTABLE_NAME") + "---" + FK.getString("PKCOLUMN_NAME") + "===" + FK.getString("FKTABLE_NAME") + "---" + FK.getString("FKCOLUMN_NAME"));
					}
					
					//GET UNIQUE COLUMNS
					ResultSet unique = metadata.getIndexInfo(null, null, name.toString(), true, false);
					System.out.println("------------UNIQUE COLUMNS-------------");
					while(unique.next())
					{
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
}
