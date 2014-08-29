package edu.asu.lerna.iolaus.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;
import edu.asu.lerna.iolaus.service.IUploadMBLDataManager;

public class UploadMBLDatasetController {
	private static final Logger logger = LoggerFactory
			.getLogger(UploadDatasetController.class);

	@Autowired
	private IUploadMBLDataManager uploadManager;

	@Autowired
	private Neo4jRegistry registry;

	@Autowired
	private INeo4jInstanceManager neo4jInstanceManager;
	
	/**
	 * This method maps the POST request for uploading Dataset to the Neo4j.
	 * 
	 * @param request
	 *            is a {@link HttpServletRequest} object.
	 * @param response
	 *            is a {@link HttpServletResponse} object.
	 * @param datasetXml
	 *            is xml which contains list of Nodes and Relationships.
	 * @return whether upload is successful or not.
	 * @throws JAXBException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/csv/affiliations", method = RequestMethod.POST)
	public String uploadAffiliationDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile)
			throws JAXBException, IOException {

		logger.info("Uploading affiliation dataset");
		File file = convertMultipartToFile(multipartfile);
		boolean success = uploadManager.uploadHasAffilationDataset(file);
		String message;
		int status;
		if(success) {
			message = "SUCCESS";
			status = 200;
		} else {
			message = "PARTIAL DATA UPLOADED";
			status = 206;
		}
		logger.info("Finished Uploading affilation dataset");
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/csv/attended", method = RequestMethod.POST)
	public String uploadAttendedDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile)
			throws JAXBException, IOException {

		logger.info("Uploading Attended dataset");
		File file = convertMultipartToFile(multipartfile);
		boolean success = uploadManager.uploadAttendedDataset(file);
		String message;
		int status;
		if(success) {
			message = "SUCCESS";
			status = 200;
		} else {
			message = "PARTIAL DATA UPLOADED";
			status = 206;
		}
		logger.info("Finished Uploading Attended dataset");
		return jsonMessage(status, message);
	}

	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/csv/investigator", method = RequestMethod.POST)
	public String uploadIvestigatorDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile)
			throws JAXBException, IOException {

		logger.info("Uploading investigator dataset");
		File file = convertMultipartToFile(multipartfile);
		boolean success = uploadManager.uploadHasInvestigatorDataset(file);
		String message;
		int status;
		if(success) {
			message = "SUCCESS";
			status = 200;
		} else {
			message = "PARTIAL DATA UPLOADED";
			status = 206;
		}
		logger.info("Finished Uploading investigator dataset");
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/csv/location", method = RequestMethod.POST)
	public String uploadLocationDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile)
			throws JAXBException, IOException {

		logger.info("Uploading location dataset");
		File file = convertMultipartToFile(multipartfile);
		boolean success = uploadManager.uploadHasLocationDataset(file);
		String message;
		int status;
		if(success) {
			message = "SUCCESS";
			status = 200;
		} else {
			message = "PARTIAL DATA UPLOADED";
			status = 206;
		}
		logger.info("Finished Uploading location dataset");
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/csv/partof", method = RequestMethod.POST)
	public String uploadIsPartOfDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile)
			throws JAXBException, IOException {

		logger.info("Uploading IsPartOf dataset");
		File file = convertMultipartToFile(multipartfile);
		boolean success = uploadManager.uploadIsPartOfDataset(file);
		String message;
		int status;
		if(success) {
			message = "SUCCESS";
			status = 200;
		} else {
			message = "PARTIAL DATA UPLOADED";
			status = 206;
		}
		logger.info("Finished Uploading IsPartOf dataset");
		return jsonMessage(status, message);
	}
	
	private File convertMultipartToFile(MultipartFile multipartfile)
			throws IOException {
		File file = new File(multipartfile.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(multipartfile.getBytes());
		fos.close();
		return file;
	}
	
	private String jsonMessage(int status, String message) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"status\" : " + status + ",");
		json.append("\"message\" : " + "\"" + message + "\"");
		json.append("}");
		return json.toString();
	}

}
