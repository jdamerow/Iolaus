package edu.asu.lerna.iolaus.domain.dataset;

import edu.asu.lerna.iolaus.domain.dataset.impl.PropertyList;

public interface IRelation {

	long getId();

	void setId(long value);

	String getType();

	void setType(String value);

	PropertyList getPropertyList();

	void setPropertyList(PropertyList value);

	long getStartNode();

	void setStartNode(long value);

	long getEndNode();

	void setEndNode(long value);

}
