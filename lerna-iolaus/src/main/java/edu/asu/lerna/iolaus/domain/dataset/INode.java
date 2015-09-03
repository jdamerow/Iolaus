package edu.asu.lerna.iolaus.domain.dataset;

import java.util.List;


public interface INode {

	public long getId();

	public void setId(long value);

	public List<IProperty> getPropertyList();

	public void setPropertyList(List<IProperty> value);

	public String getLabel();

	public void setLabel(String value);

	public String getJsonNode();

	public List<String> getNodeAsJsonForIndexing(String uri);
}
