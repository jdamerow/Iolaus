package edu.asu.lerna.iolaus.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.Query;
import edu.asu.lerna.iolaus.service.IQueryManager;
import edu.asu.lerna.iolaus.service.IXMLtoCypherConverter;

@Service
public class QueryManager implements IQueryManager {

	@Autowired
	public IXMLtoCypherConverter xmlToCypherConverter;
	
	private static final Logger logger = LoggerFactory
			.getLogger(QueryManager.class);
	
	@Override
	public void parseQuery(Query q){
		xmlToCypherConverter.parseQuery(q);
	}
	
	/**
	 * Use Unmarshaller to unmarshal the XMl into Query object
	 * @param res
	 * @author Lohith Dwaraka
	 */
	@Override
	public Query xmlToQueryObject(String res) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 =  unmarshaller.unmarshal(new StreamSource(is), Query.class);
		Query q = response1.getValue();
		return q;
	}
	
	@Override
	public String getRESTOutput()
	{
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<resultset>\n	<nodes>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstname\" value=\"Jennie C. R.\" />\n			<property name=\"lastname\" value=\"Smith\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstname\" value=\"John H.\" />\n			<property name=\"lastname\" value=\"Northrup\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstname\" value=\"Joseph C.\" />\n			<property name=\"lastname\" value=\"Herrick\" />\n		</node>\n	</nodes>\n	<relations>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9120\">\n			<property name=\"sourcetype\" value=\"Person\" />\n			<property name=\"targettype\" value=\"Location\" />\n			<property name=\"sourceid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\" />\n			<property name=\"targetid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value =\"1902\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9121\">\n			<property name=\"sourcetype\" value=\"Person\" />\n			<property name=\"targettype\" value=\"Location\" />\n			<property name=\"sourceid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\" />\n			<property name=\"targetid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value =\"1911\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9122\">\n			<property name=\"sourcetype\" value=\"Person\" />\n			<property name=\"targettype\" value=\"Location\" />\n			<property name=\"sourceid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value =\"1914\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9123\">\n			<property name=\"sourcetype\" value=\"Person\" />\n			<property name=\"targettype\" value=\"Location\" />\n			<property name=\"sourceid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetid\" value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value =\"1915\" />\n		</relation>\n	</relations>\n</resultset>";
	}
}
