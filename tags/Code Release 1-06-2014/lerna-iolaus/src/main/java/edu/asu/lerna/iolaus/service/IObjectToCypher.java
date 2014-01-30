package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;

/**
 * This class will convert Node or RelNode object to cypher
 * @author Karan Kothari
 *
 */
public interface IObjectToCypher {
	/**
	 * This method will convert {@link Node} to the cypher query.
	 * @param node is a {@link Node} object.
	 * @return the object of {@link ReturnParametersOfOTC}.
	 */
	ReturnParametersOfOTC objectToCypher(INode node);
	/**
	 * This method will convert {@link RelNode} object to the cypher query.
	 * @param node is a {@link RelNode} object.
	 * @return the object of {@link ReturnParametersOfOTC}
	 */
	ReturnParametersOfOTC objectToCypher(IRelNode node);
	
	String s="Start ";
	String m="Match ";
	String w="Where ";
	String r="return ";
}