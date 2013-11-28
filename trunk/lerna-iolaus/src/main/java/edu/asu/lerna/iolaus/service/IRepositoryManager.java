package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.misc.Tree;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IRepositoryManager {

	public abstract void queryToCypher(IQuery q);

	public abstract Tree breakdownQuery(IQuery q);

	public abstract void executeQuery(IQuery q);

}
