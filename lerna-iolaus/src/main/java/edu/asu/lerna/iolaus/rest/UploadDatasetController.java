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

/**
 * This controller has mapping for uploading dataset to the Neo4j. Endpoint of URI is /uploadDataset
 * @author Karan Kothari
 */

@Controller
public class UploadDatasetController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UploadDatasetController.class);
	
	@Autowired
	private IUploadManager uploadManager;
	
	/**
	 * This method maps the POST request for uploading Dataset to the Neo4j. 
	 * @param request is a {@link HttpServletRequest} object.
	 * @param response is a {@link HttpServletResponse} object.
	 * @param datasetXml is xml which contains list of Nodes and Relationships.
	 * @return whether upload is successful or not.
	 * @throws JAXBException
	 */
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
