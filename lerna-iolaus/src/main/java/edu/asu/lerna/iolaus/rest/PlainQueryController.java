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
import edu.asu.lerna.iolaus.service.IPlainQueryManager;

/**
 * This controller maps the url request /plainquery.
 * It takes plain query as an input and executes it.
 * @author Karan Kothari
 */

@Controller
public class PlainQueryController {
	@Autowired
	private IPlainQueryManager plainQueryManager;
	
	@Autowired
	private ErrorMessage errorMessage;
	
	private static final Logger logger = LoggerFactory
			.getLogger(PlainQueryController.class);
	/**
	 * 
	 * @param request is a {@link HttpServletRequest} object
	 * @param response is a {@link HttpServletResponse} object
	 * @param res is a input plain query input xml
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/query/plainquery", method = RequestMethod.GET)
	public String queryIolaus(HttpServletRequest request,	HttpServletResponse response,@RequestBody String res){
		
		if(res == null || res.isEmpty()){
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
		outputXml = plainQueryManager.executeQuery(res);
		response.setStatus(200);
		}catch(SAXException e){//invalid xml
			String err=e.toString();
			outputXml=errorMessage.getErrorMsg(err.substring(err.indexOf("lineNumber")), request);
			logger.error(e.getMessage());
		} catch (IOException e) {
			outputXml=errorMessage.getErrorMsg("There is some problem in reading the file",request);
			e.printStackTrace();
		} catch (JAXBException e) {
			outputXml=errorMessage.getErrorMsg("Error in the input Xml",request);
			logger.error(e.getMessage());
		}
		return outputXml;
	}
}
