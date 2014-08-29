package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public abstract class Node {
	public abstract String toJson() throws JsonGenerationException, JsonMappingException, IOException;
	public abstract String getUri();
}
