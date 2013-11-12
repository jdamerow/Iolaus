package edu.asu.lerna.iolaus.domain.queryobject;

import java.util.LinkedHashMap;


public interface IRelNodeFinderData {

	public abstract LinkedHashMap<String, IRelNode> getNodeList();

	public abstract void setNodeList(LinkedHashMap<String, IRelNode> nodeList);

	public abstract String getPath();

	public abstract void setPath(String path);

}