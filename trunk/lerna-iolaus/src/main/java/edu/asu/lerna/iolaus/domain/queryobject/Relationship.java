//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.24 at 10:35:31 AM MST 
//


package edu.asu.lerna.iolaus.domain.queryobject;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}source" minOccurs="0"/>
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}target" minOccurs="0"/>
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}property" minOccurs="0"/>
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}and" minOccurs="0"/>
 *         &lt;element ref="{http://digitalhps.org/lerna-query-model}or" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="return" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sourceOrTargetOrProperty"
})
@XmlRootElement(name = "relationship")
public class Relationship {

    @XmlElementRefs({
        @XmlElementRef(name = "or", namespace = "http://digitalhps.org/lerna-query-model", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "target", namespace = "http://digitalhps.org/lerna-query-model", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "and", namespace = "http://digitalhps.org/lerna-query-model", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "property", namespace = "http://digitalhps.org/lerna-query-model", type = Property.class, required = false),
        @XmlElementRef(name = "source", namespace = "http://digitalhps.org/lerna-query-model", type = JAXBElement.class, required = false)
    })
    protected List<Object> sourceOrTargetOrProperty;
    @XmlAttribute(name = "return")
    protected Boolean _return;

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
     * {@link JAXBElement }{@code <}{@link Operator }{@code >}
     * {@link JAXBElement }{@code <}{@link Operator }{@code >}
     * {@link Property }
     * {@link JAXBElement }{@code <}{@link RelNode }{@code >}
     * {@link JAXBElement }{@code <}{@link RelNode }{@code >}
     * 
     * 
     */
    public List<Object> getSourceOrTargetOrProperty() {
        if (sourceOrTargetOrProperty == null) {
            sourceOrTargetOrProperty = new ArrayList<Object>();
        }
        return this.sourceOrTargetOrProperty;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturn(Boolean value) {
        this._return = value;
    }

}
