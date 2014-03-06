package edu.asu.lerna.iolaus.service;


public interface IDeleteDatasetManager {
	/**
	 * This method deletes the Nodes and Relations having dataset provided by the user.
	 * @param dataset is dataset provided by the user.
	 * @return true if all the nodes and relations are deleted else it will return false. 
	 */
	boolean deleteDataset(String dataset);
}
