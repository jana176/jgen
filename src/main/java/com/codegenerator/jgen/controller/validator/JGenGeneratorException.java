package com.codegenerator.jgen.controller.validator;

import lombok.Getter;

public class JGenGeneratorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;
	
	@Getter
	private String tableName;
	
	public JGenGeneratorException(final String message, final String tableName) {
		super(message);
		this.message = message;
		this.tableName = tableName;
	}
}
