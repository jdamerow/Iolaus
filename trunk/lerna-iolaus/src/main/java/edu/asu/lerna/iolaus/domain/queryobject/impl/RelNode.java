//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.25 at 09:21:19 AM MST 
//


package edu.asu.lerna.iolaus.domain.queryobject.impl;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;


/**
 * <p>Java class for rel_node complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rel_node">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}node"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rel_node", propOrder = {
    "node"
})
public class RelNode implements IRelNode {
	
	private static final Logger logger = LoggerFactory
			.getLogger(RelNode.class);

    @XmlElement(required = true)
    protected INode node;

    /* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNode#getNode()
	 */
    @Override
	public INode getNode() {
        return node;
    }

    /* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNode#setNode(edu.asu.lerna.iolaus.domain.queryobject.INode)
	 */
    @Override
	public void setNode(INode value) {
        this.node = value;
    }
    
    /* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNode#parseRelNode(edu.asu.lerna.iolaus.domain.queryobject.impl.IRelNode)
	 */
    @Override
	public void parseRelNode(IRelNode relNode){
    	INode node = relNode.getNode();
    	
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(o instanceof Property){
    			logger.info("Found Property Object :");
    			IProperty prop = (IProperty) o;
    			prop.parseProperty(prop);
    		}
    		
    	}
    }

    public static class Adapter extends XmlAdapter<RelNode,IRelNode> {
    	public IRelNode unmarshal(RelNode v) { return v; }
    	public RelNode marshal(IRelNode v) { return (RelNode)v; }

     }
}
