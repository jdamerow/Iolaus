package edu.asu.lerna.iolaus.domain.queryobject;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.queryobject.impl.Database;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Dataset;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;

@XmlJavaTypeAdapter(Query.Adapter.class)
public interface IQuery {

	/**
	 * Gets the value of the database property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Database }
	 *     
	 */
	public abstract IDatabase getDatabase();

	/**
	 * Sets the value of the database property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Database }
	 *     
	 */
	public abstract void setDatabase(IDatabase value);

	/**
	 * Gets the value of the dataset property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Dataset }
	 *     
	 */
	public abstract IDataset getDataset();

	/**
	 * Sets the value of the dataset property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Dataset }
	 *     
	 */
	public abstract void setDataset(IDataset value);

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

	/**
	 * Gets the value of the relationship property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Relationship }
	 *     
	 */
	public abstract IRelationship getRelationship();

	/**
	 * Sets the value of the relationship property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Relationship }
	 *     
	 */
	public abstract void setRelationship(IRelationship value);

	/**
	 * Gets the value of the database ID.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Database }
	 *     
	 */
	public abstract String getDatabaseId(IDatabase db);

	/**
	 * Gets the value of the dataset name.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Dataset }
	 *     
	 */
	public abstract String getDatasetName(IDataset ds);

	/**
	 * Gets the value of the dataset name.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Dataset }
	 *     
	 */
	public abstract void getNodeDetails(INode node);

}