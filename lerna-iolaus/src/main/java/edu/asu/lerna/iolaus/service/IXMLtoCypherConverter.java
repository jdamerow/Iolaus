package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBElement;

import edu.asu.lerna.iolaus.domain.queryobject.Query;

public interface IXMLtoCypherConverter {

	void parseQuery(JAXBElement<Query> response);

}
