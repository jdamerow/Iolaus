package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.List;

import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

public class CacheManager implements ICacheManager {

	//TODO: Change this implementation
	private List<IRepositoryHandler> repoHanlders;

	public CacheManager()
	{
		repoHanlders = new ArrayList<IRepositoryHandler>();
	}
}
