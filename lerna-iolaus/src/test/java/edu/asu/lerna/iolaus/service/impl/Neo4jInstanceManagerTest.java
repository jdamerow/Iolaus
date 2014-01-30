package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
	public void testAddNeo4jInstance() throws UnsupportedEncodingException {
		
		String classPath=Neo4jInstanceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();//gets the class path
		File folder=new File(URLDecoder.decode(classPath.substring(0,classPath.indexOf("classes")),"UTF-8")+"classes/ConfigurationFiles");
		assertEquals(neo4jRegistry.getfileList().size(), folder.list().length);
		//Try to null entry to the registry
		assertNull(instanceManager.addNeo4jInstance(null));
		//Try to add empty instance
		assertNull(instanceManager.addNeo4jInstance(new Neo4jInstance()));
		//Try to add an instance with the port number on which server is not running and isActive=true
		assertEquals("-2", instanceManager.addNeo4jInstance(instance));
		assertEquals(neo4jRegistry.getfileList().size(), folder.list().length);
		
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
			assertEquals(neo4jRegistry.getfileList().size(), folder.list().length);
		}
		//valid instance
		maxId=getMaxId();
		instance.setActive(false);
		instance.setHost("localhost");
		instance.setPort("1203");
		assertEquals(String.valueOf(Integer.parseInt(maxId)+1), instanceManager.addNeo4jInstance(instance));
		assertEquals(neo4jRegistry.getfileList().size(), folder.list().length);
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
	public void testUpdateNeo4jInstance() throws UnsupportedEncodingException, IOException {
		
		if(neo4jRegistry.getfileList()!=null){
			INeo4jInstance existingInstance=neo4jRegistry.getfileList().get(0);
			String id=existingInstance.getId();
			String host=existingInstance.getHost();
			String port=existingInstance.getPort();
			//already existing instance
			assertEquals(0,instanceManager.updateNeo4jInstance(existingInstance));
			assertEquals(id,neo4jRegistry.getfileList().get(0).getId());
			assertEquals(host,neo4jRegistry.getfileList().get(0).getHost());
			assertEquals(port,neo4jRegistry.getfileList().get(0).getPort());
		}
		//null instance
		assertEquals(-1,instanceManager.updateNeo4jInstance(null));
		
		//invalid instance id
		instance.setId("-1");
		instance.setActive(false);
		assertEquals(-2, instanceManager.updateNeo4jInstance(instance));
		
		//duplicate combination of host and port
		if(neo4jRegistry.getfileList().size()>=2){
			INeo4jInstance newInstace=neo4jRegistry.getfileList().get(0);
			INeo4jInstance existingInstance=neo4jRegistry.getfileList().get(1);
			newInstace.setHost(existingInstance.getHost());
			newInstace.setPort(existingInstance.getPort());
			assertEquals(1, instanceManager.updateNeo4jInstance(newInstace));
		}
		
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
	public void testGetInstanceId() {
		assertNull(instanceManager.getInstanceId(null, null));
	}

}
