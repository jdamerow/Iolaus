package edu.asu.lerna.iolaus.domain.queryobject;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.queryobject.impl.Dataset;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;

@XmlJavaTypeAdapter(Node.Adapter.class)
@XmlSeeAlso(Node.class)
public interface INode {

	/**
	 * Gets the value of the propertyOrRelationshipOrAnd property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the propertyOrRelationshipOrAnd property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getPropertyOrRelationshipOrAnd().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Relationship }
	 * {@link JAXBElement }{@code <}{@link Operator }{@code >}
	 * {@link JAXBElement }{@code <}{@link Operator }{@code >}
	 * {@link Property }
	 * 
	 * 
	 */
	public abstract List<Object> getPropertyOrRelationshipOrAnd();

	/**
	 * Gets the value of the return property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Boolean }
	 *     
	 */
	public abstract Boolean isReturn();

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Boolean }
	 *     
	 */
	public abstract void setReturn(Boolean value);

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public abstract String getId();

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public abstract void setId(String value);

	/**
	 * Gets the details of the node.
	 * 
	 * @return
	 *     possible object in
	 *     {@link Node }
	 *     
	 */
	public abstract void getNodeDetails(
			edu.asu.lerna.iolaus.domain.queryobject.INode node);

	void getNodeRel(INode node);

}