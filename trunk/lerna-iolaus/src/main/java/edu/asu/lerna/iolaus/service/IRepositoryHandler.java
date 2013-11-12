package edu.asu.lerna.iolaus.service;

import java.util.HashMap;
import java.util.List;

public interface IRepositoryHandler {

	public abstract HashMap<String, List> executeQuery(String json);

}
