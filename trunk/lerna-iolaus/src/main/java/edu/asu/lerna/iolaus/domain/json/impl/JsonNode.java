package edu.asu.lerna.iolaus.domain.json.impl;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.asu.lerna.iolaus.domain.json.Element;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JsonNode extends Element implements IJsonNode, Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String type;
	private HashMap<String, String> data;

	public JsonNode() {
		data = new HashMap<String, String>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public HashMap<String, String> getData() {
		return data;
	}

	@Override
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	@Override
	public void addData(String key, String value) {
		if (this.data == null)
			this.data = new HashMap<String, String>();
		this.data.put(key, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonNode other = (JsonNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Adapter extends XmlAdapter<JsonNode, IJsonNode> {

		@Override
		public IJsonNode unmarshal(JsonNode v) throws Exception {
			return v;
		}

		@Override
		public JsonNode marshal(IJsonNode v) throws Exception {
			return (JsonNode) v;
		}

	}

}
