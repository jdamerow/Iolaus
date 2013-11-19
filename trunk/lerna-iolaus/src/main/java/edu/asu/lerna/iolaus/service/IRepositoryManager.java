package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IRepositoryManager {

	public abstract void queryToCypher(IQuery q);

	public abstract List<Object> breakdownQuery(IQuery q);

	public abstract void executeQuery(IQuery q);

}
