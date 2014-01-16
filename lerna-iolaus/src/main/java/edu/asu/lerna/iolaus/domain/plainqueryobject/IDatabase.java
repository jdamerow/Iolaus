package edu.asu.lerna.iolaus.domain.plainqueryobject;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.Database;

@XmlJavaTypeAdapter(Database.Adapter.class)
@XmlSeeAlso(Database.class)
public interface IDatabase {

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

}