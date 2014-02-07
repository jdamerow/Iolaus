package edu.asu.lerna.iolaus.domain.dataset;

import edu.asu.lerna.iolaus.domain.dataset.impl.PropertyList;

public interface INode {

	long getId();

	void setId(long value);

	PropertyList getPropertyList();

	void setPropertyList(PropertyList value);

}
