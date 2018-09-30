package com.codegenerator.jgen.handler.model;

import javax.validation.Valid;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Service {

	@Builder.Default
	private Boolean generateService = true;

	@Valid
	private ServiceOperations serviceOperations;
	
}
