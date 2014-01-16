package edu.asu.lerna.iolaus.domain.plainqueryobject.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.IDatabase;
import edu.asu.lerna.iolaus.domain.plainqueryobject.IDatabaseList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "databaseList", propOrder = {
    "database"
})
public class DatabaseList implements IDatabaseList {
	
	 	@XmlJavaTypeAdapter(Database.Adapter.class) protected List<IDatabase> database;

	    /**
	     * Gets the value of the database property.
	     * 
	     * <p>
	     * This accessor method returns a reference to the live list,
	     * not a snapshot. Therefore any modification you make to the
	     * returned list will be present inside the JAXB object.
	     * This is why there is not a <CODE>set</CODE> method for the database property.
	     * 
	     * <p>
	     * For example, to add a new item, do as follows:
	     * <pre>
	     *    getDatabase().add(newItem);
	     * </pre>
	     * 
	     * 
	     * <p>
	     * Objects of the following type(s) are allowed in the list
	     * {@link Database }
	     * 
	     * 
	     */
	    public List<IDatabase> getDatabase() {
	        if (database == null) {
	            database = new ArrayList<IDatabase>();
	        }
	        return this.database;
	    }

	    
	    public static class Adapter extends XmlAdapter<DatabaseList,IDatabaseList> {
	    	public IDatabaseList unmarshal(DatabaseList v) { return v; }
	    	public DatabaseList marshal(IDatabaseList v) { return (DatabaseList)v; }
	     }
}
