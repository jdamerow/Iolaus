package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryHandlerTest {

	@Autowired
	private QueryManager queryManager;
	@Autowired
	private QueryHandler queryHandler;
	
	private IQuery querySimple;
	
	@Before
	public void setUp() throws Exception {
		String classPath=URLDecoder.decode(FragmentQueryTest.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
		//Create query object of simple node.
		BufferedReader br=new BufferedReader(new FileReader(classPath.substring(0,classPath.indexOf("classes"))+"classes/SampleInput1.xml"));
		String line="";
		String file="";
		while((line=br.readLine())!=null){
			file=file+line;
		}
		br.close();
		querySimple=queryManager.xmlToQueryObject(file);
	}

	@Test
	public void testExecuteQuery() {
		
		assertNull(queryHandler.executeQuery(null));
		assertNull(queryHandler.executeQuery(new Query()));
		assertNotNull(queryHandler.executeQuery(querySimple));
		assertNotNull(queryHandler.executeQuery(querySimple).getResultSet());
		assertNotSame(0, queryHandler.executeQuery(querySimple).getResultSet().size());
		assertEquals(3,queryHandler.executeQuery(querySimple).getResultSet().size());
	}

}
