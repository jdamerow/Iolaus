package edu.asu.lerna.iolaus.domain.queryobject.impl;

import java.util.LinkedHashMap;

import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNodeFinderData;

public class RelNodeFinderData implements IRelNodeFinderData {

	LinkedHashMap<String, IRelNode> nodeList;
	String path="";
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNodeFinderData#getNodeList()
	 */
	@Override
	public LinkedHashMap<String, IRelNode> getNodeList() {
		return nodeList;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNodeFinderData#setNodeList(java.util.LinkedHashMap)
	 */
	@Override
	public void setNodeList(LinkedHashMap<String, IRelNode> nodeList) {
		this.nodeList = nodeList;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNodeFinderData#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNodeFinderData#setPath(java.lang.String)
	 */
	@Override
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
