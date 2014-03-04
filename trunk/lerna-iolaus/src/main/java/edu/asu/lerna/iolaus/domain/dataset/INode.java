package edu.asu.lerna.iolaus.domain.dataset;

import java.util.List;


public interface INode {

	long getId();

	void setId(long value);

	List<IProperty> getPropertyList();

	void setPropertyList(List<IProperty> value);

	String getLabel();

	void setLabel(String value);

	String getJsonNode();

}