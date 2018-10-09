package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControllerOperations {

	@Builder.Default
	private String controllerPath = "generated";
	
	@Builder.Default
	private Boolean get = true;
	
	@Builder.Default
	private Boolean post = false;
	
	@Builder.Default
	private Boolean put = false;
	
	@Builder.Default
	private Boolean delete = false;
}
