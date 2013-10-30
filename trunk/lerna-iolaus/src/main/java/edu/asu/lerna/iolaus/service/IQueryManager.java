package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBException;

import edu.asu.lerna.iolaus.domain.queryobject.Query;

public interface IQueryManager {

	void parseQuery(Query q);

	Query xmlToQueryObject(String res) throws JAXBException;

	String getRESTOutput();

}
