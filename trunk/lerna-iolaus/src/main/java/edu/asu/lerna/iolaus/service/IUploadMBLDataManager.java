package edu.asu.lerna.iolaus.service;

import java.io.File;
import java.io.IOException;

import edu.asu.lerna.iolaus.exception.IndexPropertyException;
import edu.asu.lerna.iolaus.exception.Neo4jInstanceIdDoesNotExist;

public interface IUploadMBLDataManager {
	
	String nodeEntryPoint="node";
	String relationEntryPoint="relationships";
	String indexNameEntryPoint="index";
	String nodeIndexEntryPoint="node";
	
	String regex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	
	boolean uploadAttendedDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist;
	boolean uploadAffilationDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist;
	boolean uploadInvestigatorDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist;
	boolean uploadLocationDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist;
	boolean uploadCourseGroupDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist;
}
