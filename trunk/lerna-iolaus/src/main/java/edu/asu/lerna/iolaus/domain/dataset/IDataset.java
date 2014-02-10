package edu.asu.lerna.iolaus.domain.dataset;

import java.util.List;

public interface IDataset {

	List<String> getDatabaseList();

	void setDatabaseList(List<String> value);

	List<INode> getNodeList();

	void setNodeList(List<INode> value);

	List<IRelation> getRelationList();

	void setRelationList(List<IRelation> value);

	String getDatasetType();

	void setDatasetType(String value);

}
