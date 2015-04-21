package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;

/**
 * This class converts Node or RelNode object to cypher
 * @author Karan Kothari
 *
 */
public interface IXMLToCypherConverter {
	/**
	 * This method converts {@link Node} to the cypher query.
	 * @param node is a {@link Node} object.
	 * @param dataSet is a dataSet of the Objects in the Neo4j
	 * @param nodeIndexName is a name of an index to which all properties of nodes have been added. Use Neo4jRegistry to get its value depending on instance id.
	 * @return the object of {@link ReturnParametersOfOTC}.
	 */
	ReturnParametersOfOTC objectToCypher(INode node,String dataSet, String nodeIndexName);
	/**
	 * This method converts {@link RelNode} object to the cypher query.
	 * @param node is a {@link RelNode} object.
	 * @param dataSet is a dataSet of the Objects in the Neo4j
	 * @return the object of {@link ReturnParametersOfOTC}
	 */
	ReturnParametersOfOTC objectToCypher(IRelNode node,String dataSet, String nodeIndexName);
	
	void createStableQuery(IQuery query);
	
	String s="Start ";
	String m="Match ";
	String w="Where ";
	String r="return ";
	String createStableQuery(INode node, String id, String nodeIndex);
	
	public final String FORWARD = "to";
	public final String BACKWARD = "from";
}
