package edu.asu.lerna.iolaus.domain.queryobject;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.queryobject.impl.Dataset;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;

@XmlJavaTypeAdapter(Dataset.Adapter.class)
@XmlSeeAlso(Dataset.class)
public interface IDataset {

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