package edu.asu.lerna.iolaus.rest;

import javax.servlet.http.HttpServletRequest;
import edu.asu.lerna.iolaus.domain.queryobject.CypherQuery;
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

import edu.asu.lerna.iolaus.domain.queryobject.Query;
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
	 * @throws JAXBException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryiolaus", method = RequestMethod.POST)
	public String queryIolaus(HttpServletRequest request,	HttpServletResponse response,@RequestBody String res,@RequestHeader("Accept") String accept) throws JAXBException {
		
		if(res == null || res.isEmpty()){
			response.setStatus(400);
			return "Query XML is empty";
		}
		Query q =  queryManager.xmlToQueryObject(res);
		if(q == null){
			response.setStatus(400);
			return "failure";
		}
		queryManager.parseQuery(q);
		response.setStatus(200);
		logger.info(CypherQuery.where);
		return queryManager.getRESTOutput();
	}
}
