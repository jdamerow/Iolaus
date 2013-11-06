package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IXMLtoCypherConverter {

	void parseQuery(IQuery q);

}
