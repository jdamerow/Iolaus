package edu.asu.lerna.iolaus.domain.plainqueryobject;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.Query;

@XmlJavaTypeAdapter(Query.Adapter.class)
public interface IQuery {

	void setCypher(String value);

	List<String> getDatabaseList();

	void setDatabaseList(List<String> value);

	String getCypher();

}
