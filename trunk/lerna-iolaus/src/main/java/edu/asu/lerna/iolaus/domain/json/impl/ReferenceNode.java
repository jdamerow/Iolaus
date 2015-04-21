package edu.asu.lerna.iolaus.domain.json.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReferenceNode {
	private String id;

	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}
}
