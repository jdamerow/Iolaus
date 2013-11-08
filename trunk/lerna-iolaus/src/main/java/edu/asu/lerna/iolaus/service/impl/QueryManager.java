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

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.service.IObjecttoXMLConverter;
import edu.asu.lerna.iolaus.service.IQueryManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;
import edu.asu.lerna.iolaus.service.IXMLtoCypherConverter;

@Service
public class QueryManager implements IQueryManager {

	@Autowired
	private IXMLtoCypherConverter xmlToCypherConverter;
	
	@Autowired
	private IObjecttoXMLConverter objectToXMLConverter;
	
	@Autowired
	private IRepositoryManager repositoryManager;
	
	private static final Logger logger = LoggerFactory
			.getLogger(QueryManager.class);
	
	
	@Override
	public String executeQuery(String inputXML) throws JAXBException
	{
		//Parse the xml and generate the query object from it
		IQuery q =  this.xmlToQueryObject(inputXML);
		if(q == null){
			return "failure";
		}
		
		//Parse the query generated from the xml and get node, relation objects
		//xmlToCypherConverter.parseQuery(q);	
		
		//This stage should return the nodes and relations
		repositoryManager.executeQuery(q);
		
		
		
		//TODO: Make a decision to send nodes or relations or both in xml
		//TODO: call this.getRESTOutput with the decision
		
		return "test";
	}
	
	
	/**
	 * Use Unmarshaller to unmarshal the XMl into Query object
	 * @param res
	 * @author Lohith Dwaraka
	 */
	@Override
	public IQuery xmlToQueryObject(String res) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 =  unmarshaller.unmarshal(new StreamSource(is), Query.class);
		IQuery q = response1.getValue();
		return q;
	}
	
	
	
	@Override
	public String getRESTOutput(IQuery q, boolean wantNodes, boolean wantRelations)
	{
		if(wantNodes && wantRelations)
		{
			//TODO: From the query return both the node and relation.
			return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<resultset>\n	<nodes>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"Jennie C. R.\" />\n			<property name=\"lastName\" value=\"Smith\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"John H.\" />\n			<property name=\"lastName\" value=\"Northrup\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"Joseph C.\" />\n			<property name=\"lastName\" value=\"Herrick\" />\n		</node>\n	</nodes>\n	<relations>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9120\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1902\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9121\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1911\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9122\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1914\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9123\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1915\" />\n		</relation>\n	</relations>\n</resultset>\n";
		}
		else if(wantNodes)
		{
			//TODO: From the query return only the node
			return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<resultset>\n	<nodes>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"Jennie C. R.\" />\n			<property name=\"lastName\" value=\"Smith\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"John H.\" />\n			<property name=\"lastName\" value=\"Northrup\" />\n		</node>\n		<node id=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\">\n			<property name=\"type\" value=\"Person\" />\n			<property name=\"firstName\" value=\"Joseph C.\" />\n			<property name=\"lastName\" value=\"Herrick\" />\n		</node>\n	</nodes>\n</resultset>\n";
		}
		else if(wantRelations)
		{
			//TODO: From the query return only the relation
			return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<resultset>\n	<relations>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9120\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13123\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1902\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9121\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13124\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1911\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9122\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1914\" />\n		</relation>\n		<relation id=\"http://www.diging.asu.edu/lerna/mblcourses/relations/9123\">\n			<property name=\"sourceType\" value=\"Person\" />\n			<property name=\"targetType\" value=\"Location\" />\n			<property name=\"sourceId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/13125\" />\n			<property name=\"targetId\"				value=\"http://www.diging.asu.edu/lerna/mblcourses/nodes/234\" />\n			<property name=\"year\" value=\"1915\" />\n		</relation>\n	</relations>\n</resultset>\n";
		}
		
		
		return "You did not request for any node or relationship! After all this work, you did not want to see the output...";
	}
}
