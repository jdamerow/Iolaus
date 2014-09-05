package edu.asu.lerna.iolaus.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.exception.IndexPropertyException;
import edu.asu.lerna.iolaus.exception.Neo4jInstanceIdDoesNotExist;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;
import edu.asu.lerna.iolaus.service.IUploadMBLDataManager;

@Controller
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
	 * @throws IndexPropertyException 
	 * @throws Neo4jInstanceIdDoesNotExist 
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/{instanceId}/csv/affiliation", method = RequestMethod.POST)
	public String uploadAffiliationDataset(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,Principal principal,
			@RequestParam("file") MultipartFile multipartfile, @PathVariable("instanceId") String instanceId)
			throws JAXBException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {

		File file = convertMultipartToFile(multipartfile);
		String fileName = file.getName();
		String message;
		int status;
		if((fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase("csv"))) {
			logger.info("Uploading affiliation dataset");
			boolean success = uploadManager.uploadAffilationDataset(file, instanceId);
			if(success) {
				message = "SUCCESS";
				status = 200;
			} else {
				message = "PARTIAL DATA UPLOADED";
				status = 206;
			}
			logger.info("Finished Uploading affilation dataset");
		} else {
			status = 406;
			message = "File format must be CSV";
		}
		
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/{instanceId}/csv/attendance", method = RequestMethod.POST)
	public String uploadAttendedDataset(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,Principal principal,
			@RequestParam("file") MultipartFile multipartfile,  @PathVariable("instanceId") String instanceId)
			throws JAXBException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {

		File file = convertMultipartToFile(multipartfile);
		String fileName = file.getName();
		String message;
		int status;
		if((fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("csv"))) {
			logger.info("Uploading Attended dataset");
			boolean success = uploadManager.uploadAttendedDataset(file, instanceId);
			if(success) {
				message = "SUCCESS";
				status = 200;
			} else {
				message = "PARTIAL DATA UPLOADED";
				status = 206;
			}
			logger.info("Finished Uploading Attended dataset");
		} else {
			status = 406;
			message = "File format must be CSV";
		}
		return jsonMessage(status, message);
	}

	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/{instanceId}/csv/investigator", method = RequestMethod.POST)
	public String uploadIvestigatorDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile, @PathVariable("instanceId") String instanceId)
			throws JAXBException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {

		File file = convertMultipartToFile(multipartfile);
		String fileName = file.getName();
		String message;
		int status;
		if((fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("csv"))) {
			logger.info("Uploading investigator dataset");
			boolean success = uploadManager.uploadInvestigatorDataset(file, instanceId);
			if(success) {
				message = "SUCCESS";
				status = 200;
			} else {
				message = "PARTIAL DATA UPLOADED";
				status = 206;
			}
			logger.info("Finished Uploading investigator dataset");
		} else {
			status = 406;
			message = "File format must be CSV";
		}
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/{instanceId}/csv/location", method = RequestMethod.POST)
	public String uploadLocationDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile, @PathVariable("instanceId") String instanceId)
			throws JAXBException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {

		File file = convertMultipartToFile(multipartfile);
		String fileName = file.getName();
		String message;
		int status;
		if((fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("csv"))) {
			logger.info("Uploading location dataset");
			boolean success = uploadManager.uploadLocationDataset(file, instanceId);
			if(success) {
				message = "SUCCESS";
				status = 200;
			} else {
				message = "PARTIAL DATA UPLOADED";
				status = 206;
			}
			logger.info("Finished Uploading location dataset");
		} else {
			status = 406;
			message = "File format must be CSV";
		}
		return jsonMessage(status, message);
	}
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/mbl/upload/{instanceId}/csv/coursegroup", method = RequestMethod.POST)
	public String uploadIsPartOfDataset(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile multipartfile, @PathVariable("instanceId") String instanceId)
			throws JAXBException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {

		File file = convertMultipartToFile(multipartfile);
		String fileName = file.getName();
		String message;
		int status;
		if((fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("csv"))) {
			logger.info("Uploading CourseGroup dataset");
			boolean success = uploadManager.uploadCourseGroupDataset(file, instanceId);
			if(success) {
				message = "SUCCESS";
				status = 200;
			} else {
				message = "PARTIAL DATA UPLOADED";
				status = 206;
			}
			logger.info("Finished Uploading IsPartOf dataset");
		} else {
			status = 406;
			message = "File format must be CSV";
		}
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
