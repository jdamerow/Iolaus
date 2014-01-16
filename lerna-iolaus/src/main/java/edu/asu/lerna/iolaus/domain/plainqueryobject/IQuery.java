package edu.asu.lerna.iolaus.domain.plainqueryobject;

import java.util.List;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.DatabaseList;
import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.Query;

@XmlJavaTypeAdapter(Query.Adapter.class)
public interface IQuery {

	/**
	 * Gets the value of the databaseList property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link DatabaseList }
	 *     
	 */
	public abstract List<String> getDatabaseList();

	/**
	 * Sets the value of the databaseList property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link DatabaseList }
	 *     
	 */
	public abstract void setDatabaseList(List<String> value);

	/**
	 * Gets the value of the cypher property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public abstract String getCypher();

	/**
	 * Sets the value of the cypher property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public abstract void setCypher(String value);

}