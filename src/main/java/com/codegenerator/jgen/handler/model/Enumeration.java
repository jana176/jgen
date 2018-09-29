package com.codegenerator.jgen.handler.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Enumeration {

	@NotEmpty
	private Visibility visibility;
	
	@NotEmpty
	private String enumType;
	
	private List<String> values;
	
}
