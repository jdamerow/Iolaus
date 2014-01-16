package edu.asu.lerna.iolaus.domain.plainqueryobject;

import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.IDatabase;
import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.Database;
import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.DatabaseList;

@XmlJavaTypeAdapter(DatabaseList.Adapter.class)
@XmlSeeAlso(DatabaseList.class)
public interface IDatabaseList {

	
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
    public abstract List<IDatabase> getDatabase();
}
