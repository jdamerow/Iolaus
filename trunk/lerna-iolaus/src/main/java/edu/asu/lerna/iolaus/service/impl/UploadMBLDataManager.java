package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.asu.lerna.iolaus.domain.mbl.nodes.Course;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Node;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Person;
import edu.asu.lerna.iolaus.service.IUploadMBLDataManager;

public class UploadMBLDataManager implements IUploadMBLDataManager {

	//Data format - Year,Last Name,Person URI,Course Name,First Name,Role,Course URI
	@Override
	public boolean uploadAttendedDataset(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		boolean success = true; 
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Node from = null;
			Node to = null;
			if(data.length >= 7) {
				
				//From node
				if(data[2] != null && data[4] != null && data[1] != null) {
					from = new Person(data[2].trim(), data[4].trim(), data[1].trim());
				} else {
					success = false;
				}
				
				//To Node
				if(data[6] != null && data[3] != null && data[0] != null) {
					int year;
					try {
						year = Integer.parseInt(data[0].trim());
					} catch (NumberFormatException e) {
						year = 0;
					}
					to = new Course(data[6].trim(), data[3].trim(), year);
				} else {
					success = false;
				}
				
				//Relation (Create Relation after uploading nodes
			}
		}
		br.close();
		return success;
	}

	@Override
	public boolean uploadHasAffilationDataset(File file) {
		return false;
	}

	@Override
	public boolean uploadHasInvestigatorDataset(File file) {
		return false;
	}

	@Override
	public boolean uploadHasLocationDataset(File file) {
		return false;
	}

	@Override
	public boolean uploadIsPartOfDataset(File file) {
		return false;
	}
}
