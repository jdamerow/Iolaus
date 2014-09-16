package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheManagerTest {

	@Autowired
	private CacheManager cacheManager;

	String json = "{ \"query\" : \"start s=node(*),t=node(*) Match s-[r]->t return s,r,t\" }";
	String neo4jInstance = "http://localhost:7476/db/data/cypher";

	@Test
	public void testGetKey() {
		//Valid case
		assertEquals("85510b7ff8b463ac98b02c7f14d51a2b",cacheManager.getKey(json, "http://localhost:7474/db/data/cypher"));
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
	
	@Test
	public void testCacheNodeId() {
		String jsonQuery = "{ \"query\" : \"start s=node:new_index(lastname={param1}) match s-[r:attendance]->t return s,r,t\", \"params\" : { \"param1\" : \"Setchell\"} }";
		String instance = "http://localhost:7476/db/data/cypher";
		//cache a node
		cacheManager.cacheNodeId("http://history.archives.mbl.edu/concepts/person/dbe9dab2-83ae-480d-9adf-b8b3c1805483", instance, "41");
		//cache query results
		cacheManager.executeQuery(jsonQuery, instance);
		//Results should be there in the memcache
		assertNotNull(cacheManager.getCachedResults(jsonQuery, instance));
		String key = cacheManager.getKey(jsonQuery, instance);
		//delete cached node
		cacheManager.delete(key);
		//reinsert the same node
		cacheManager.cacheNodeId("http://history.archives.mbl.edu/concepts/person/dbe9dab2-83ae-480d-9adf-b8b3c1805483", instance, "41");
		//Now cached results should no longer be there.
		assertNull(cacheManager.getCachedResults(jsonQuery, instance));
	}

}
