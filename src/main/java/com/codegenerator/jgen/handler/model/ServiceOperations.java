package com.codegenerator.jgen.handler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceOperations {

	@Builder.Default
	private Boolean findById = true;
	
	@Builder.Default
	private Boolean findAll = true;
	
	@Builder.Default
	private Boolean save = true;
	
	@Builder.Default
	private Boolean delete = true;
}
