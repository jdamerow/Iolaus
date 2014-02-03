package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ObjectToCypherTest {
	
	private IQuery querySimple;
	private IQuery queryNested;

	@Autowired
	private QueryManager queryManager;
	@Autowired
	private ObjectToCypher objectToCypher;
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
		
		//Create query object of nested Nodes.
		br=new BufferedReader(new FileReader(classPath.substring(0,classPath.indexOf("classes"))+"classes/SampleInput5.xml"));
		file=new String();
		while((line=br.readLine())!=null){
			file=file+line;
		}
		br.close();
		queryNested=queryManager.xmlToQueryObject(file);
	}

	@Test
	public void testObjectToCypherINodeString() {
		
		INode node=null;
		//null node object
		assertNull(objectToCypher.objectToCypher(node, null));
		
		//empty node object
		assertNotNull(objectToCypher.objectToCypher(new Node(), null));
		
		ReturnParametersOfOTC returnObject=objectToCypher.objectToCypher(querySimple.getNode(), querySimple.getDataset().getId());
		
		//valid node object
		assertNotNull(returnObject);
		
		Map<Object,String> objectToTargetLabelMap=returnObject.getObjectToTargetLabelMap();
		Map<String,Boolean> isReturnMap=returnObject.getIsReturnMap();
		String json=returnObject.getJson();
	
		assertNotNull(objectToTargetLabelMap);
		assertNotNull(isReturnMap);
		assertNotNull(json);
		
		assertEquals(3, objectToTargetLabelMap.size());
		assertEquals(3, isReturnMap.size());
		
		assertTrue(isReturnMap.get(PropertyOf.SOURCE.toString()));
		assertTrue(isReturnMap.get(PropertyOf.RELATION.toString()+1));
		assertFalse(isReturnMap.get(PropertyOf.TARGET.toString()+1));
		
		Set<Object> nodeObjects=objectToTargetLabelMap.keySet();
		int counter=0;
		for(Object object:nodeObjects){
			if(counter==0)
				assertEquals(PropertyOf.SOURCE.toString(),objectToTargetLabelMap.get(object));
			if(counter==1)
				assertEquals(PropertyOf.RELATION.toString()+1,objectToTargetLabelMap.get(object));
			if(counter==2)
				assertEquals(PropertyOf.TARGET.toString()+1,objectToTargetLabelMap.get(object));
			counter++;
		}
	}

	@Test
	public void testObjectToCypherIRelNodeString() {
		IRelNode node=null;
		//null node object
		assertNull(objectToCypher.objectToCypher(node, null));
		
		//empty node object
		assertNotNull(objectToCypher.objectToCypher(new RelNode(), null));
		
		
		
		ReturnParametersOfOTC returnObject=objectToCypher.objectToCypher(queryNested.getNode(), querySimple.getDataset().getId());
		
		//valid node object
		assertNotNull(returnObject);
		
		Map<Object,String> objectToTargetLabelMap=returnObject.getObjectToTargetLabelMap();
		Map<String,Boolean> isReturnMap=returnObject.getIsReturnMap();
		String json=returnObject.getJson();
	
		assertNotNull(objectToTargetLabelMap);
		assertNotNull(isReturnMap);
		assertNotNull(json);
		
		assertEquals(3, objectToTargetLabelMap.size());
		assertEquals(3, isReturnMap.size());
		
		assertTrue(isReturnMap.get(PropertyOf.SOURCE.toString()));
		assertTrue(isReturnMap.get(PropertyOf.RELATION.toString()+1));
		assertFalse(isReturnMap.get(PropertyOf.TARGET.toString()+1));
		
		Set<Object> nodeObjects=objectToTargetLabelMap.keySet();
		int counter=0;
		for(Object object:nodeObjects){
			if(counter==0)
				assertEquals(PropertyOf.SOURCE.toString(),objectToTargetLabelMap.get(object));
			if(counter==1)
				assertEquals(PropertyOf.RELATION.toString()+1,objectToTargetLabelMap.get(object));
			if(counter==2)
				assertEquals(PropertyOf.TARGET.toString()+1,objectToTargetLabelMap.get(object));
			counter++;
		}
	}

}
