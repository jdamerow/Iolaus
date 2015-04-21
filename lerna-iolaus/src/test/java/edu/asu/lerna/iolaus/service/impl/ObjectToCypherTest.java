package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ObjectToCypherTest {
	
	private IQuery querySimple;
	private IQuery queryNested;
	String json1= "{\"query\":\"Start source=node:node_auto_index(type={param1}) , target1=node:node_auto_index(type={param2})      " +
			"Match source-[r1]->target1     " +
			"Where (( source.dataset=~{param3} ) and (( r1.type=~{param4} and r1.year=1900 ) and (( target1.label=~{param5} and target1.dataset=~{param3} )) ) ) " +
			"return source, target1, r1\",\n" +
			"\"params\":{\"param1\":\"Person\"," +
						"\"param2\":\"Series\"," +
						"\"param3\":\"(?i).*mblcourses.*\"," +
						"\"param4\":\"(?i).*attended.*\"," +
						"\"param5\":\"(?i).*Botany.*\"," +
						"\"param6\":\"(?i).*mblcourses.*\"}}";
		
	String json2="{\"query\":\"Start source=node:node_auto_index(type={param1}) , target1=node:node_auto_index(type={param2})      " +
			"Match source-[r1]->target1     " +
			"Where (( source.dataset=~{param3} ) and (( r1.type=~{param4} ) and (( target1.label=~{param5} and target1.dataset=~{param3} )) ) ) " +
			"return source, target1, r1\",\n"+
			"\"params\":{\"param1\":\"Person\"," +
						"\"param2\":\"Institute\"," +
						"\"param3\":\"(?i).*mblcourses.*\"," +
						"\"param4\":\"(?i).*affiliatedWith.*\"," +
						"\"param5\":\"(?i).*Harvard.*\"," +
						"\"param6\":\"(?i).*mblcourses.*\"}}";

	@Autowired
	private QueryManager queryManager;
	@Autowired
	private XMLToCypherConverter xmlToCypher;
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
		assertNull(xmlToCypher.objectToCypher(node, null,null));
		
		//empty node object
		assertNotNull(xmlToCypher.objectToCypher(new Node(), null, null));
		
		ReturnParametersOfOTC returnObject=xmlToCypher.objectToCypher(querySimple.getNode(), querySimple.getDataset().getId(), "new_index");
		
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
		//validate the json query
		assertEquals(json1,json);
		
	}

	@Test
	public void testObjectToCypherIRelNodeString() {
		INode node=null;
		IRelNode relNode=null;
		//null node object
		assertNull(xmlToCypher.objectToCypher(node, null, null));
		
		//empty node object
		assertNotNull(xmlToCypher.objectToCypher(new RelNode(), null, null));
		
		node=queryNested.getNode();
		relNode=getRelNode(node);
		
		ReturnParametersOfOTC returnObject=xmlToCypher.objectToCypher(relNode, queryNested.getDataset().getId(), "new_index");
		
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
		
		//validate the json query
		assertEquals(json2,json);
	}

	@SuppressWarnings("unchecked")
	private IRelNode getRelNode(INode node) {
		
		IRelNode relNode=null;
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		if(nodeDetailsIterator.hasNext()){
			Object obj=nodeDetailsIterator.next();
			JAXBElement<?> element = (JAXBElement<Object>) obj;
			IOperator op = (IOperator) element.getValue();
			List<Object> objectList = op.getSourceOrTargetOrProperty();
			IRelationship rel = (IRelationship) objectList.get(1);
			JAXBElement<?> relElement = (JAXBElement<?>) rel.getSourceOrTargetOrProperty().get(1);
			relNode=(IRelNode)relElement.getValue();
		}
		return relNode;
	}

}
