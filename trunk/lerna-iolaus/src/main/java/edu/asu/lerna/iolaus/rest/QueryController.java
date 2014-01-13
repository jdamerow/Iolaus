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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.service.IQueryManager;

@Controller
public class QueryController {

	@Autowired
	private IQueryManager queryManager;
	
	private static final Logger logger = LoggerFactory
			.getLogger(QueryController.class);
	/**
	 * 
	 * @param request
	 * @param response
	 * @param res
	 * @param accept
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryiolaus", method = RequestMethod.POST)
	public String queryIolaus(HttpServletRequest request,	HttpServletResponse response,@RequestBody String res,@RequestHeader("Accept") String accept){
		
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
		outputXml = queryManager.executeQuery(res);
		response.setStatus(200);
		}catch(SAXException e){
			String err=e.toString();
			outputXml=queryManager.getErrorMsg(err.substring(err.indexOf("lineNumber")), request);
			logger.error(e.getMessage());
		} catch (IOException e) {
			outputXml=queryManager.getErrorMsg("There is some problem in reading the file",request);
			e.printStackTrace();
		} catch (JAXBException e) {
			outputXml=queryManager.getErrorMsg("Error in the input Xml",request);
			logger.error(e.getMessage());
		}
		return outputXml;
	}
}
