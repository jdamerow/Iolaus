package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IRepositoryManager {

	public abstract void queryToCypher(IQuery q);

	public abstract void breakdownQuery(IQuery q);

	public abstract void executeQuery(IQuery q);

}