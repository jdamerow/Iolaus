package edu.asu.lerna.iolaus.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping(value = "/queryiolaus", method = RequestMethod.POST)
	public String queryIolaus(HttpServletRequest request,
			HttpServletResponse response,@RequestBody String res,@RequestHeader("Accept") String accept) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 =  unmarshaller.unmarshal(new StreamSource(is), Query.class);
		if(response1 == null){
			response.setStatus(400);
			return "failure";
		}
		
		logger.info("Success");
		response.setStatus(200);
		return "success";
	}
}
