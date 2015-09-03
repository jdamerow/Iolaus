package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBException;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.dataset.IDataset;
import edu.asu.lerna.iolaus.exception.UploadDatasetException;

public interface IUploadManager {

	String datasetType="dataset";
	String nodeEntryPoint="node";
	String relationEntryPoint="relationships";
	String indexNameEntryPoint="index";
	String nodeIndexEntryPoint="node";
	String relationIndexEntryPoint="relationship";
	
	/**
	 * This method adds datasetXml into Neo4j instances specified in xml
	 * @param dataset is input xml
	 * @return whether dataset is added or not
	 * @throws JAXBException
	 * @throws UploadDatasetException 
	 */
	public boolean uploadDataset(IDataset dataset) throws JAXBException, UploadDatasetException;

	public String makeRESTCall(String entryPointUri, String json, INeo4jInstance neo4jInstance);
}
