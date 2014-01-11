package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"file:src/test/resources/misc-beans.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RepositoryHandlerTest {

	@Autowired
	private RepositoryHandler repoHandler;
	
	String json = "{ \"query\" : \"start s=node(*),t=node(*) Match s-[r]->t return s,r,t\" }";
	String neo4jInstance = "http://localhost:7474/db/data/";
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testExecuteQuery() {
		//Execute the correct json query on the instance.
		assertNotNull(repoHandler.executeQuery(json, neo4jInstance));
		
		//Execute an empty json on a correct instance.
		assertNull(repoHandler.executeQuery(null, neo4jInstance));
		
		//Execute a correct json on a null instance.
		assertNull(repoHandler.executeQuery(json, null));
		
		//Null json and null instance
		assertNull(repoHandler.executeQuery(null, null));
	}

}
