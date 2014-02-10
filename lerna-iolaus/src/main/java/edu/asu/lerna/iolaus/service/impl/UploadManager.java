package edu.asu.lerna.iolaus.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import edu.asu.lerna.iolaus.domain.dataset.IDataset;
import edu.asu.lerna.iolaus.domain.dataset.impl.Dataset;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.service.IUploadManager;

public class UploadManager implements IUploadManager{

	@Override
	public boolean uploadDataset(String datasetXml) {
		
		return false;
	}
	
	public IDataset xmlToObject(String res) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Dataset.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Dataset> response =  unmarshaller.unmarshal(new StreamSource(is), Dataset.class);
		IDataset dataset = response.getValue();
		return dataset;
	}
	
}
