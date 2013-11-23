package edu.asu.lerna.iolaus.domain.json;

import java.util.HashMap;

public interface IJsonNode {

	public abstract void setData(HashMap<String, String> data);

	public abstract HashMap<String, String> getData();

	public abstract void setType(String type);

	public abstract String getType();

	public abstract void setId(String id);

	public abstract String getId();

	public abstract void addData(String key, String value);

}