package com.codegenerator.jgen.handler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for string manipulation
 *
 */
public final class ClassNamesUtil {

	public static String toClassName(String tableName) {	
		
		StringBuilder builder = new StringBuilder();

		String newTableName = defineSingular(tableName);

		String parts[] = newTableName.split("_");

		List<String> listedParts = Arrays.asList(parts);
		listedParts.forEach(part -> {
			String capitalizedWord = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
			builder.append(capitalizedWord);
		});
		
		return builder.toString();
	}

	public static String toFieldName(String fieldName) {
		String camelCasePattern = "([a-z]+[A-Z]+\\w+)+";
		if(fieldName.matches(camelCasePattern))
			return fieldName;
		StringBuilder builder = new StringBuilder();
		String initialSplit[] = fieldName.split("_", 2);

		String firstWord = initialSplit[0];
		builder.append(firstWord.toLowerCase());

		if (initialSplit.length > 1) {
			String otherWords = initialSplit[1].toString();
			String parts[] = otherWords.split("_");
			List<String> listedParts = Arrays.asList(parts);
			listedParts.forEach(part -> {
				String capitalizedWord = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
				builder.append(capitalizedWord);
			});
		}

		return builder.toString();
	}

	public static List<String> separateEnumValues(String enumType) {
		String enumerations = enumType.replaceAll("enum", "");
		String splits[] = enumerations.split(",");
		List<String> parts = Arrays.asList(splits);
		List<String> clearedParts = new ArrayList<>();

		parts.forEach(part -> {
			clearedParts.add(part.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", ""));
		});
		return clearedParts;
	}

	private static String defineSingular(final String plural) {
		if (plural.toLowerCase().endsWith("s")) {
			return plural.substring(0, plural.length() - 1);
		} else {
			return plural;
		}
	}
}
