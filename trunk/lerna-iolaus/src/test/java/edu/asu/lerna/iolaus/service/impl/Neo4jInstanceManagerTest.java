package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jInstance;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class Neo4jInstanceManagerTest {

	@Autowired 
	private Neo4jRegistry neo4jRegistry;
	
	@Autowired
	private Neo4jInstanceManager instanceManager;
	
	private Neo4jInstance instance;
	
	@Before
	public void setUp() throws Exception {
		instance=new Neo4jInstance();
		instance.setActive(true);
		instance.setDbPath("db/data");
		instance.setDescription("test");
		instance.setHost("localhost");
		instance.setPort("1000");
		instance.setUserName("testUser");
	}

	@Test
	public void testAddNeo4jInstance() {
		
		//Try to null entry to the registry
		assertNull(instanceManager.addNeo4jInstance(null));
		//Try to add empty instance
		assertNull(instanceManager.addNeo4jInstance(new Neo4jInstance()));
		//Try to add an instance with the port number on which server is not running and isActive=true
		assertEquals("-2", instanceManager.addNeo4jInstance(instance));
		String existingPort="";
		String existingHost="";
		String maxId="0";
		if(neo4jRegistry.getfileList().size()!=0){
			existingPort=neo4jRegistry.getfileList().get(0).getPort();
			existingHost=neo4jRegistry.getfileList().get(0).getHost();
			instance.setActive(false);
			instance.setPort(existingPort);
			instance.setHost(existingHost);
			//Try to add an instance which already exists
			assertEquals("0", instanceManager.addNeo4jInstance(instance));
		}
		maxId=getMaxId();
		instance.setActive(false);
		instance.setHost("localhost");
		instance.setPort("1203");
		assertEquals(String.valueOf(Integer.parseInt(maxId)+1), instanceManager.addNeo4jInstance(instance));
	}

	private String getMaxId() {
		String maxId="0";
		for(INeo4jInstance ins:neo4jRegistry.getfileList()){
			if(Integer.parseInt(ins.getId())>Integer.parseInt(maxId)){
				maxId=ins.getId();
			}
		}
	return maxId;
	}

	@Test
	public void testDeleteNeo4jInstance() throws IOException {
		int prevSize=neo4jRegistry.getfileList().size();
		//send null id
		instanceManager.deleteNeo4jInstance(null);
		assertEquals(prevSize, neo4jRegistry.getfileList().size());
		
		//send invalid id
		instanceManager.deleteNeo4jInstance("-1");
		assertEquals(prevSize, neo4jRegistry.getfileList().size());
		
		//send valid id
		instanceManager.deleteNeo4jInstance(getMaxId());
		assertEquals(prevSize-1, neo4jRegistry.getfileList().size());
		
	}

	@Test
	public void testUpdateNeo4jInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInstance() {
		//Id is null
		assertNull(instanceManager.getInstance(null));
		
		//Invalid id
		assertNull(instanceManager.getInstance("-1"));
		
		//valid id
		assertNotNull(instanceManager.getInstance(getMaxId()));
		assertEquals(getMaxId(), instanceManager.getInstance(getMaxId()).getId());
	}

	@Test
	public void testGetAllInstances() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInstanceId() {
		fail("Not yet implemented");
	}

}
