package com.codegenerator.jgen.handler.model;

import javax.validation.Valid;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Controller {

	@Builder.Default
	private Boolean generateController = true;
	
	@Valid
	private ControllerOperations controllerOperations;
}
