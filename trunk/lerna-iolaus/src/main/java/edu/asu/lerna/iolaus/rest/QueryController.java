package edu.asu.lerna.iolaus.rest;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.error.ErrorMessage;
import edu.asu.lerna.iolaus.service.IQueryManager;

/**
 * This controller has mapping for /xmlquery. It accepts the query in XML format.. 
 * @author Karan Kothari
 *
 */

@Controller
public class QueryController {

	@Autowired
	private IQueryManager queryManager;
	
	@Autowired
	private ErrorMessage errorMessage;
	
	private static final Logger logger = LoggerFactory
			.getLogger(QueryController.class);
	/**
	 * This method has mapping for POST request for /queryiolaus. 
	 * @param request is {@link HttpServletRequest} object.
	 * @param response is {@link HttpServletResponse} object.
	 * @param query is a query in XML format. 
	 * @return the results in the XML format.
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/query/xmlquery", method = RequestMethod.POST)
	public String queryIolaus(HttpServletRequest request,	HttpServletResponse response,@RequestBody String query){
		
		if(query == null || query.isEmpty()){
			response.setStatus(400);
			return "Query XML is empty";
		}
		
		/**
		 * Controller does not know the internals of QueryManager. It only receives
		 * the input xml and uses query manager to produce the xml output.
		 */
		String outputXml=null;
		try{
		//Execute the input request and fetch outputxml from QueryManager
		outputXml = queryManager.executeQuery(query);
		response.setStatus(200);
		}catch(SAXException e){
			String err=e.toString();
			outputXml=errorMessage.getErrorMsg(err.substring(err.indexOf("lineNumber")), request);
			logger.error(e.getMessage());
		} catch (IOException e) {
			outputXml=errorMessage.getErrorMsg("There is some problem in reading the file",request);
			e.printStackTrace();
		} catch (JAXBException e) {
			outputXml=errorMessage.getErrorMsg("Error in the input Xml",request);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return outputXml;
	}
	
}
