//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.25 at 09:21:19 AM MST 
//


package edu.asu.lerna.iolaus.domain.queryobject;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="end" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "property")
public class Property {


	private static final Logger logger = LoggerFactory
			.getLogger(Property.class);
	
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "start")
    protected BigInteger start;
    @XmlAttribute(name = "end")
    protected BigInteger end;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStart(BigInteger value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEnd(BigInteger value) {
        this.end = value;
    }

    
    public void parseProperty(Property prop,PropertyOf propertyOf){
    	String value=propertyOf.toString();
    	if((!propertyOf.equals(PropertyOf.RELATION))&&(!CypherQuery.nodeRel.containsKey(value))){
    		CypherQuery.nodeRel.put(propertyOf, value);
    	}
    	
    	String op="and";
    	
    	if(!CypherQuery.operator.empty()){
    		//CypherQuery.where+=") ";
    		//op=CypherQuery.operator.pop()+" ( ";
    		op=CypherQuery.operator.pop();
    		logger.info("Operator in property:"+op);
    	}
    	
    	if(prop.getValue()!=null){
    		//logger.info("Property Name : "+prop.getName() );
        	if(CypherQuery.where.equals("where "))
        		CypherQuery.where+=value+"."+prop.getName()+"="+prop.getValue()+" ";
       		else
       			CypherQuery.where+=op+" "+value+"."+prop.getName()+"="+prop.getValue()+" ";
       		/*if(op.contains("(")){
       			op.replaceAll("( ", "");
       		}*/
       		logger.info("Property Value : "+prop.getValue() );
    	} 
    	else{
    		
    		if(prop.getType()!=null){
	    		logger.info("Property Type : "+prop.getType() );
	    		if(prop.getStart()!=null){
	        		if(CypherQuery.where.equals("where "))
	        			CypherQuery.where+=value+"."+prop.getName()+">="+prop.getStart()+" ";
	        		else
	        			CypherQuery.where+=op+" "+value+"."+prop.getName()+">="+prop.getStart()+" ";
	        		/*if(op.contains("(")){
	           			op.replaceAll("( ", "");
	        		}*/
	        		logger.info("Property Start : "+prop.getStart() );
	    		}
    		}
    		if(prop.getEnd()!=null){
        		if(CypherQuery.where.equals("where "))
        			CypherQuery.where+=" "+value+"."+prop.getName()+"<="+prop.getEnd()+" ";
        		else
        			CypherQuery.where+=op+" "+value+"."+prop.getName()+"<="+prop.getEnd()+" ";
        		logger.info("Property End : "+prop.getEnd() );
    		}
    	}
    	
    	if(prop.getId()!=null){
    		logger.info("Property ID : "+prop.getId() );
    	}	
    	
    }
}
