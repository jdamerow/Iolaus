package edu.asu.lerna.iolaus.service.impl;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.Query;
import edu.asu.lerna.iolaus.service.IXMLtoCypherConverter;

@Service
public class XMLtoCypherConverter implements IXMLtoCypherConverter{
	
	private static final Logger logger = LoggerFactory
			.getLogger(XMLtoCypherConverter.class);
	
	@Override
	public void parseQuery(JAXBElement<Query> response){
		
		Query q = response.getValue();
		logger.info("Database ID : "+q.getDatabaseId(q.getDatabase()));
		logger.info("Dataset Name : "+q.getDatasetName(q.getDataset()));
		if(q.getNode()!=null){
			logger.info("Found a Node, Following are node details");
			q.getNodeDetails(q.getNode());
		}
	}
}
