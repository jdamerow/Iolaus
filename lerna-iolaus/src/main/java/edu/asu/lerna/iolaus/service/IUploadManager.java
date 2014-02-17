package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBException;

public interface IUploadManager {

	String datasetType="dataset";
	String nodeEntryPoint="node";
	String relationEntryPoint="relationships";
	String indexNameEntryPoint="index";
	String nodeIndexEntryPoint="node";
	String relationIndexEntryPoint="relationship";
	boolean uploadDataset(String datasetXml) throws JAXBException;
}
