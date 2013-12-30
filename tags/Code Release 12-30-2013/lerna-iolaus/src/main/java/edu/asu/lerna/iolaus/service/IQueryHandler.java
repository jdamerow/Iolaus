package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.misc.ResultSet;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IQueryHandler {

	public abstract ResultSet executeQuery(IQuery q);

}
