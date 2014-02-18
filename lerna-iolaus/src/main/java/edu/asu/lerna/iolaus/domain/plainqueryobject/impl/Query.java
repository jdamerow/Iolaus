//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.01.15 at 05:24:44 PM MST 
//


package edu.asu.lerna.iolaus.domain.plainqueryobject.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.IQuery;


/**
 * <p>Java class for query complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="query">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="databaseList" type="{http://digitalhps.org/lerna-plainquery-model}databaseList"/>
 *         &lt;element name="cypher" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query", propOrder = {
    "databaseList",
    "cypher"
})
public class Query implements IQuery {

	@XmlElementWrapper(name="databaseList")
    @XmlElement(name="database")
	protected List<String> databaseList;
    @XmlElement(required = true)
    protected String cypher;

    /**
	 * {@inheritDoc}
	*/
    @Override
	public List<String> getDatabaseList() {
        return databaseList;
    }

    /**
	 * {@inheritDoc}
	*/
    @Override
	public void setDatabaseList(List<String> value) {
        this.databaseList = value;
    }

    /**
	 * {@inheritDoc}
	*/
    @Override
	public String getCypher() {
        return cypher;
    }

    /**
	 * {@inheritDoc}
	*/
    @Override
	public void setCypher(String value) {
        this.cypher = value;
    }
    
    public static class Adapter extends XmlAdapter<Query,IQuery> {
    	public IQuery unmarshal(Query v) { return v; }
    	public Query marshal(IQuery v) { return (Query)v; }

     }

}