package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Nodes {
	@XmlJavaTypeAdapter(StepNodes.Adapter.class)
	private List<StepNodes> stepNodes;
	
	
	
	public List<StepNodes> getStepNodes() {
		return stepNodes;
	}



	public void setStepNodes(List<StepNodes> node) {
		this.stepNodes = node;
	}



	public void addStepNode(StepNodes stepNodes) {
		if(this.stepNodes == null) {
			this.stepNodes = new ArrayList<StepNodes>();
		}
		
		this.stepNodes.add(stepNodes);
		
	}
}
