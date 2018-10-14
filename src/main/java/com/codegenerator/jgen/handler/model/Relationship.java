package com.codegenerator.jgen.handler.model;

import com.codegenerator.jgen.handler.model.enumeration.RelationshipType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Relationship {

	@Builder.Default
	private Boolean isRelationshipClass = false;
	
	private RelationshipType relationshipType;

}
