package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"file:src/test/resources/misc-beans.xml","file:src/test/resources/memcached.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheManagerTest {

	@Autowired
	private CacheManager cacheManager;

	String json = "{ \"query\" : \"start s=node(*),t=node(*) Match s-[r]->t return s,r,t\" }";
	String neo4jInstance = "http://localhost:7474/db/data/cypher";

	@Test
	public void testGetKey() {
		//Valid case
		assertEquals("85510b7ff8b463ac98b02c7f14d51a2b",cacheManager.getKey(json, neo4jInstance));
	}

	@Test
	public void testExecuteQuery() {
		//Execute the correct json query on the instance.
		assertNotNull(cacheManager.executeQuery(json, neo4jInstance));

		//Execute an empty json on a correct instance.
		assertNull(cacheManager.executeQuery(null, neo4jInstance));

		//Execute a correct json on a null instance.
		assertNull(cacheManager.executeQuery(json, null));

		//Null json and null instance
		assertNull(cacheManager.executeQuery(null, null));
	}

}
