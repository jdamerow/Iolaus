package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.json.Element;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IStepNodes;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepNodes extends Element implements IStepNodes {

	@XmlJavaTypeAdapter(JsonNode.Adapter.class)
	private Set<IJsonNode> node = null;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Set<IJsonNode> getNode() {
		return node;
	}

	@Override
	public void setNode(Set<IJsonNode> stepNodes) {
		this.node = stepNodes;
	}

	@Override
	public void addNode(IJsonNode jsonNode) {
		if(node == null) {
			node = new LinkedHashSet<IJsonNode>();
		}
		
		node.add(jsonNode);
	}

	@Override
	public void addNodes(List<IJsonNode> stepNodes) {
		if(this.node == null) {
			this.node = new LinkedHashSet<IJsonNode>();
		}
		
		for(IJsonNode jsonNode : stepNodes) {
			this.node.add(jsonNode);
		}
	}
	
	public static class Adapter extends XmlAdapter<StepNodes, IStepNodes>
	{

		@Override
		public IStepNodes unmarshal(StepNodes v) throws Exception {
			return v;
		}

		@Override
		public StepNodes marshal(IStepNodes v) throws Exception {
			return (StepNodes)v;
		}
		
	}
	
}
