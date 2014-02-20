package edu.asu.lerna.iolaus.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.exception.UploadDatasetException;
import edu.asu.lerna.iolaus.service.IUploadManager;

@Controller
public class UploadDatasetController {
	private static final Logger logger = LoggerFactory
			.getLogger(UploadDatasetController.class);
	@Autowired
	private IUploadManager uploadManager;
	
	@RequestMapping(value = "/uploadDataset", method = RequestMethod.POST)
	public String uploadDataset(HttpServletRequest request,	HttpServletResponse response,@RequestBody String datasetXml) throws JAXBException {
		logger.info("Uploading the dataset");
		try{
			uploadManager.uploadDataset(datasetXml);
		}catch(JAXBException jaxBexception){
			
		}catch(UploadDatasetException uploadDatasetException){
			
		}catch(Exception unknownException){
			
		}
		logger.info("Uploding finished");
		return null;
	}
}
