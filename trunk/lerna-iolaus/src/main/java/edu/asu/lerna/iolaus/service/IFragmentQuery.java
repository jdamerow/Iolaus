package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;


public interface IFragmentQuery {
	
	/**
	 * This method will break down a query object into multiple json queries
	 * @param q is a Query object
	 * @return the object of {@link LabelTree} 
	 */
	LabelTree breakdownQuery(IQuery q);
}
