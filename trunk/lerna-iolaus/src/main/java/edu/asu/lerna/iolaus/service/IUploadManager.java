package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBException;

public interface IUploadManager {

	String datasetType="dataset";
	String nodeEntryPoint="node";
	String relationEntryPoint="relationships";
	String indexNameEntryPoint="index";
	String nodeIndexEntryPoint="node";
	String relationIndexEntryPoint="relationship";
	
	/**
	 * This method adds datasetXml into Neo4j instances specified in xml
	 * @param datasetXml is input xml
	 * @return whether dataset is added or not
	 * @throws JAXBException
	 */
	boolean uploadDataset(String datasetXml) throws JAXBException;
}
