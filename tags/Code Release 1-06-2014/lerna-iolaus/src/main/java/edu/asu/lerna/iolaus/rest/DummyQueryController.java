package edu.asu.lerna.iolaus.rest;

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

import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.service.IQueryManager;

//@Controller
public class DummyQueryController {

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
	 * @throws JAXBException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryiolaus", method = RequestMethod.POST)
	public String queryIolaus(HttpServletRequest request,	HttpServletResponse response,@RequestBody String res,@RequestHeader("Accept") String accept) throws JAXBException {
		
		if(res == null || res.isEmpty()){
			response.setStatus(400);
			return "Query XML is empty";
		}
		
		String outputXml = null;
		Query q = null;
		
		/**
		 * TODO:Remove the if conditions after complete implementation
		 * as this check will be done in getRESTOutput method based on q 
//		 */
//		if(res.contains("node return=\"true\"") && res.contains("relationship return=\"true\""))
//		{
//			outputXml = queryManager.getRESTOutput(q,true,true);
//		}
//		else if(res.contains("node return=\"true\""))
//		{
//			outputXml = queryManager.getRESTOutput(q,true,false);
//		}
//		else if(res.contains("relationship return=\"true\""))
//		{
//			outputXml = queryManager.getRESTOutput(q,false,true);
//		}
//		else
//		{
//			outputXml = queryManager.getRESTOutput(q,false,false);
//		}
//		
		
		return outputXml;
	}
}
