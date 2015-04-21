package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.json.impl.Resultset;
import edu.asu.lerna.iolaus.domain.misc.ResultSet;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;

/**
 * This class executes the {@link IQuery} object and returns the {@link ResultSet}  
 * @author Karan Kothari
 *
 */
public interface IQueryHandler {

	/**
	 * This method executes the query object and creates the {@link ResultSet}
	 * @param q is a {@link Query} object
	 * @return the {@link ResultSet}
	 */
	public abstract ResultSet executeQuery(IQuery q);

	Resultset executeStableQuery(IQuery q);

}
