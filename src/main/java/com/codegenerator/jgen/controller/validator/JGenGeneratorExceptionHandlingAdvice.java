package com.codegenerator.jgen.controller.validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Data;

@ControllerAdvice
public class JGenGeneratorExceptionHandlingAdvice {

	@ExceptionHandler({ JGenGeneratorException.class })
	public ResponseEntity<Response> invalidParameters(JGenGeneratorException ex) {
		String message = ex.getMessage();
		String tableName = ex.getTableName();
		Response response = new Response();
		response.setMessage(message);
		response.setTable(tableName);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@Data
	private static class Response {

		private String message;
		
		private String table;
	}
}
