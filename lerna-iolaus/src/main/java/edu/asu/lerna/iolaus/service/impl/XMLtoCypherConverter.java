package edu.asu.lerna.iolaus.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.service.IXMLtoCypherConverter;

@Service
public class XMLtoCypherConverter implements IXMLtoCypherConverter{
	
	private static final Logger logger = LoggerFactory
			.getLogger(XMLtoCypherConverter.class);
	
	@Override
	public void parseQuery(IQuery q){
		
		logger.info("Database ID : "+q.getDatabaseId(q.getDatabase()));
		logger.info("Dataset Name : "+q.getDatasetName(q.getDataset()));
		if(q.getNode()!=null){
			logger.info("Found a Node, Following are node details");
			q.getNodeDetails(q.getNode());
		}
		if(q.getRelationship()!=null){
			logger.info("Found a Relationship, Following are Relationship details");
			//q.getRelationshipDetails(q.getRelationship());
		}
	}
}
