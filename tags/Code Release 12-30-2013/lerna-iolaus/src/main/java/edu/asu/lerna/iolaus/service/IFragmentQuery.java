package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IFragmentQuery {
	LabelTree breakdownQuery(IQuery q);
}
