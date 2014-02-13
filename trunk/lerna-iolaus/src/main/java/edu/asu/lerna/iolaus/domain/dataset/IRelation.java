package edu.asu.lerna.iolaus.domain.dataset;

import java.util.List;


public interface IRelation {

	long getId();

	void setId(long value);

	String getType();

	void setType(String value);

	List<IProperty> getPropertyList();

	void setPropertyList(List<IProperty> value);

	long getStartNode();

	void setStartNode(long value);

	long getEndNode();

	void setEndNode(long value);

	String getJsonRelation(String endNode);

}
