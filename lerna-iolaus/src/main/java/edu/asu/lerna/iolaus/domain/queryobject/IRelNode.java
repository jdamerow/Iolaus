package edu.asu.lerna.iolaus.domain.queryobject;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;


@XmlJavaTypeAdapter(RelNode.Adapter.class)
@XmlSeeAlso(RelNode.class)
public interface IRelNode {

	/**
	 * Gets the value of the node property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Node }
	 *     
	 */
	public abstract INode getNode();

	/**
	 * Sets the value of the node property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Node }
	 *     
	 */
	public abstract void setNode(INode value);

	public abstract void parseRelNode(IRelNode relNode);

	void parseRelNodeRel(IRelNode relNode);

}