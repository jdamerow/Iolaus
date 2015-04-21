package edu.asu.lerna.iolaus.domain.json;

import java.util.List;
import java.util.Set;

public interface IStepNodes {
	Set<IJsonNode> getNode();
	void addNode(IJsonNode jsonNode);
	void addNodes(List<IJsonNode> stepNodes);
	void setNode(Set<IJsonNode> stepNodes);
}
