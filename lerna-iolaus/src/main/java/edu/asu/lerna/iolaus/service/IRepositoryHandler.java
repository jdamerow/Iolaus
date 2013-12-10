package edu.asu.lerna.iolaus.service;

import java.util.List;

public interface IRepositoryHandler {


	public abstract List<List<Object>>  executeQuery(String json, String instance);


}
