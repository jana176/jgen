package com.codegenerator.jgen.generator;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class for string manipulation
 *
 */
public final class ClassNamesUtil {

	public static String fromTableToClassName(String tableName) {
		StringBuilder builder = new StringBuilder();

		String parts[] = tableName.split("_");
		List<String> listedParts = Arrays.asList(parts);
		listedParts.forEach(part -> {
			String capitalizedWord = part.substring(0, 1) + part.substring(1).toLowerCase();
			builder.append(capitalizedWord);
		});

		return builder.toString();
	}

	public static String fromColumnNameToFieldName(String fieldName) {
		StringBuilder builder = new StringBuilder();
		String initialSplit[] = fieldName.split("_", 2);
		
		String firstWord = initialSplit[0];
		builder.append(firstWord.toLowerCase());

		String otherWords = initialSplit[1].toString();
		String parts[] = otherWords.split("_");
		List<String> listedParts = Arrays.asList(parts);
		listedParts.forEach(part -> {
			String capitalizedWord = part.substring(0, 1) + part.substring(1).toLowerCase();
			builder.append(capitalizedWord);
		});

		return builder.toString();
	}
}
