package edu.asu.lerna.iolaus.domain.implementation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Neo4jInstanceTest {
	
	private Neo4jInstance neo4jInstance;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		neo4jInstance=new Neo4jInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDbPath() {
		neo4jInstance.setDbPath(null);
		assertEquals(neo4jInstance.getId(), null);
		
		neo4jInstance.setDbPath("db/data");
		assertEquals(neo4jInstance.getDbPath(), "db/data");
	}

	@Test
	public void testSetDbPath() {
		neo4jInstance.setDbPath(null);
		assertEquals(neo4jInstance.getDbPath(), null);
		
		neo4jInstance.setDbPath("db/data");
		assertEquals(neo4jInstance.getDbPath(), "db/data");
	}

	@Test
	public void testIsActive() {
		neo4jInstance.setActive(true);
		assertEquals(neo4jInstance.isActive(),true);
	}

	@Test
	public void testSetActive() {
		neo4jInstance.setActive(true);
		assertEquals(neo4jInstance.isActive(),true);
	}

	@Test
	public void testGetId() {
		neo4jInstance.setId(null);
		assertEquals(neo4jInstance.getId(), null);
		
		neo4jInstance.setId("1");
		assertEquals(neo4jInstance.getId(), "1");
	}

	@Test
	public void testSetId() {
		neo4jInstance.setId(null);
		assertEquals(neo4jInstance.getId(), null);
		
		neo4jInstance.setId("1");
		assertEquals(neo4jInstance.getId(), "1");
	}

	@Test
	public void testGetDescription() {
		neo4jInstance.setDescription(null);
		assertEquals(neo4jInstance.getDescription(), null);
		
		neo4jInstance.setDescription("Instance is running on 7474");
		assertEquals(neo4jInstance.getDescription(),"Instance is running on 7474");
	}

	@Test
	public void testSetDescription() {
		neo4jInstance.setDescription(null);
		assertEquals(neo4jInstance.getDescription(), null);
		
		neo4jInstance.setDescription("Instance is running on 7474");
		assertEquals(neo4jInstance.getDescription(),"Instance is running on 7474");
	}

	@Test
	public void testGetPort() {
		neo4jInstance.setPort(null);
		assertEquals(neo4jInstance.getPort(), null);
		
		neo4jInstance.setPort("7474");
		assertEquals(neo4jInstance.getPort(),"7474");
	}

	@Test
	public void testSetPort() {
		neo4jInstance.setPort(null);
		assertEquals(neo4jInstance.getPort(), null);
		
		neo4jInstance.setPort("7474");
		assertEquals(neo4jInstance.getPort(),"7474");
	}

	@Test
	public void testGetHost() {
		neo4jInstance.setHost(null);
		assertEquals(neo4jInstance.getHost(), null);
		
		neo4jInstance.setHost("localhost");
		assertEquals(neo4jInstance.getHost(), "localhost");
	}
	
	@Test
	public void testSetHost() {
		neo4jInstance.setHost(null);
		assertEquals(neo4jInstance.getHost(), null);
		
		neo4jInstance.setHost("localhost");
		assertEquals(neo4jInstance.getHost(), "localhost");
	}

	@Test
	public void testGetUserName() {
		neo4jInstance.setUserName(null);
		assertEquals(neo4jInstance.getUserName(), null);
		
		neo4jInstance.setUserName("Admin");
		assertEquals(neo4jInstance.getUserName(), "Admin");
	}

	@Test
	public void testSetUserName() {
		neo4jInstance.setUserName(null);
		assertEquals(neo4jInstance.getUserName(), null);
		
		neo4jInstance.setUserName("Admin");
		assertEquals(neo4jInstance.getUserName(), "Admin");
	}

}
