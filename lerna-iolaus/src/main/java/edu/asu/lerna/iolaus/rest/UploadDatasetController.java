package edu.asu.lerna.iolaus.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.util.Random;
import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.dataset.IDataset;
import edu.asu.lerna.iolaus.domain.dataset.impl.Dataset;
import edu.asu.lerna.iolaus.exception.UploadDatasetException;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;
import edu.asu.lerna.iolaus.service.IUploadManager;

/**
 * This controller has mapping for uploading dataset to the Neo4j. Endpoint of
 * URI is /uploadDataset
 * 
 * @author Karan Kothari
 */

@Controller
public class UploadDatasetController {

	private static final Logger logger = LoggerFactory
			.getLogger(UploadDatasetController.class);

	@Autowired
	private IUploadManager uploadManager;

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
	 * @throws UploadDatasetException
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/data/uploadDataset", method = RequestMethod.POST)
	public String uploadDataset(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String datasetXml)
			throws JAXBException, UploadDatasetException {

		logger.info("Uploading the dataset");
		String randomNeo4jInstanceId = null;

		IDataset dataset = xmlToObject(datasetXml);

		if (dataset.getDatabaseList() == null
				|| dataset.getDatabaseList().size() == 0) {

			/*
			 * Neo4j instance is not specified by the herckules. Randomly select
			 * a Neo4j instance and send that instance id to the Heckules
			 */
			randomNeo4jInstanceId = selectRandomNeo4jInstance();
			List<String> databaseList = new ArrayList<String>();
			databaseList.add(randomNeo4jInstanceId);
			dataset.setDatabaseList(databaseList);

		} else {
			randomNeo4jInstanceId = dataset.getDatabaseList().get(0);
		}

		uploadManager.uploadDataset(dataset);

		logger.info("Uploding finished");

		String instanceIdInJsonForm = convertToJson(randomNeo4jInstanceId);

		return instanceIdInJsonForm;
	}

	private String convertToJson(String randomNeo4jInstanceId) {

		if (randomNeo4jInstanceId != null) {

			StringBuffer json = new StringBuffer();

			json.append("{ ");
			json.append("\"id\" : ");
			json.append("\"" + randomNeo4jInstanceId + "\"");
			json.append(" }");

			return json.toString();
		}

		return null;
	}

	/**
	 * Use Unmarshaller to unmarshal the XMl into Query object.
	 * 
	 * @param xml
	 *            is input xml
	 * @return the {@link IDataset} object after unmarshalling the xml.
	 * @throws JAXBException
	 *             when unmarshalling fails.
	 */
	private IDataset xmlToObject(String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Dataset.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller
				.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		JAXBElement<Dataset> response = unmarshaller.unmarshal(
				new StreamSource(is), Dataset.class);
		IDataset dataset = response.getValue();
		return dataset;
	}

	private String selectRandomNeo4jInstance() {

		List<INeo4jInstance> instanceList = neo4jInstanceManager
				.getActiveInstances();

		if (instanceList != null) {

			// select random number and use it as an index to fetch instanceList
			Random r = new Random();
			int index = r.nextInt(instanceList.size());
			return instanceList.get(index).getId();

		}
		return null;
	}

}
