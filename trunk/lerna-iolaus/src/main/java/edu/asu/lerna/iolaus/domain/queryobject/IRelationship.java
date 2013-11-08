package edu.asu.lerna.iolaus.domain.queryobject;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Operator;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;

@XmlJavaTypeAdapter(Relationship.Adapter.class)
@XmlSeeAlso(Relationship.class)
public interface IRelationship {

	/**
	 * Gets the value of the sourceOrTargetOrProperty property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the sourceOrTargetOrProperty property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getSourceOrTargetOrProperty().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link JAXBElement }{@code <}{@link RelNode }{@code >}
	 * {@link JAXBElement }{@code <}{@link Operator }{@code >}
	 * {@link JAXBElement }{@code <}{@link Operator }{@code >}
	 * {@link Property }
	 * {@link JAXBElement }{@code <}{@link RelNode }{@code >}
	 * 
	 * 
	 */
	public abstract List<Object> getSourceOrTargetOrProperty();

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
	public abstract void getRelationDetails(
			IRelationship relationship);

	void getRelationDetailsRel(IRelationship relationship);

}