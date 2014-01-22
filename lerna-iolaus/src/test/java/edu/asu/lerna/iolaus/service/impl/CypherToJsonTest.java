package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={
"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CypherToJsonTest {

	@Autowired
	private CypherToJson cypherToJson;
	
	String cypher1="start source=node:node_auto_index(type=\"Person\") return source";
	String json1="{\"query\":start source=node:node_auto_index(type={param1}) return source,\n\"params\":{\"param1\":\"Person\"}}";
	String cypher2="start source=node:node_auto_index(type=\"Person\") Where firstName=~\"Xyz\" return source";
	String json2="{\"query\":start source=node:node_auto_index(type={param1}) Where firstName=~{param2} return source,\n\"params\":{\"param1\":\"Person\",\"param2\":\"(?i).*Xyz.*\"}}";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testCypherToJson() {
		
		//valid case
		assertEquals(json1,cypherToJson.cypherToJson(cypher1));
		
		//valid case
		assertEquals(json2,cypherToJson.cypherToJson(cypher2));
	}

	@Test
	public void testPlainQueryToJson() {
		
		//valid case
		assertEquals(json1,cypherToJson.cypherToJson(cypher1));
	}
}
