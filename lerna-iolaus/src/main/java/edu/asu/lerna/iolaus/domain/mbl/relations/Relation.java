package edu.asu.lerna.iolaus.domain.mbl.relations;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public abstract class Relation  {
	public abstract String toJson() throws JsonGenerationException, JsonMappingException, IOException;
}
