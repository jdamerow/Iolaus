package edu.asu.lerna.iolaus.service;

import java.io.File;
import java.io.IOException;

public interface IUploadMBLDataManager {
	
	String regex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	
	boolean uploadAttendedDataset(File file) throws IOException;
	boolean uploadHasAffilationDataset(File file);
	boolean uploadHasInvestigatorDataset(File file);
	boolean uploadHasLocationDataset(File file);
	boolean uploadIsPartOfDataset(File file);
}
