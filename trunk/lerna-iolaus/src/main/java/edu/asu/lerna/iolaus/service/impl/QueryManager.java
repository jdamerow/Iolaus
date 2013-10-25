package edu.asu.lerna.iolaus.service.impl;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.Query;
import edu.asu.lerna.iolaus.service.IQueryManager;

@Service
public class QueryManager implements IQueryManager {

	
	private static final Logger logger = LoggerFactory
			.getLogger(QueryManager.class);
	
	@Override
	public void parseQuery(JAXBElement<Query> response){
		
		Query q = response.getValue();
		logger.info("Database ID : "+q.getDatabaseId(q.getDatabase()));
		logger.info("Dataset Name : "+q.getDatasetName(q.getDataset()));
		q.getNodeDetails(q.getNode());
	}
}
