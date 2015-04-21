package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.json.IJsonRelation;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Row {
	private List<ReferenceNode> referenceNodes;
	@XmlJavaTypeAdapter(JsonRelation.Adapter.class)
	private List<IJsonRelation> relations;

	public List<ReferenceNode> getNodes() {
		return referenceNodes;
	}
	
	public void setNodes(List<ReferenceNode> nodes) {
		this.referenceNodes = nodes;
	}

	public List<IJsonRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<IJsonRelation> relations) {
		this.relations = relations;
	}
	
	public void addReferenceNode(ReferenceNode referenceNode) {
		if(referenceNodes == null) {
			referenceNodes = new LinkedList<ReferenceNode>();
		}
		
		referenceNodes.add(referenceNode);
	}
	
	public void addRelation(IJsonRelation  relation) {
		if(relations == null) {
			relations = new LinkedList<IJsonRelation>();
		}
		relations.add(relation);
	}
	
}
