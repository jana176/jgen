package com.codegenerator.jgen.handler.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Enumeration {

	private Visibility visibility;
	private String enumType;
	private List<String> values;
	
}
