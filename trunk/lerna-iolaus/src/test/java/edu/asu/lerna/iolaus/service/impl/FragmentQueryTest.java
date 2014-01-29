package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.Label;
import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FragmentQueryTest {

	@Autowired
	private QueryManager queryManager;
	
	@Autowired
	private FragmentQuery fragmentQuery;
	
	private IQuery querySimple;
	private IQuery queryNested;
	
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
	public void testBreakdownQuery() {
		
		//check the Label tree if query object is null.
		assertNull(fragmentQuery.breakdownQuery(null));
		
		//check the Label tree if query object is empty.
		assertNotNull(fragmentQuery.breakdownQuery(new Query()));
		
		LabelTree tree=fragmentQuery.breakdownQuery(querySimple);
		//check the Label tree if query object is not empty
		assertNotNull(tree);
		
		//check the contents of Label tree 
		Map<String,Label> sourceToTargetLabelMap=tree.getSourceToTargetLabelMap();
		Map<String,String> targetJsonMap=tree.getTargetJsonMap();
		Map<String,String> oldLabelToNewLabelMap=tree.getOldLabelToNewLabelMap();
		Map<String,Boolean> labelToIsReturnTrueMap=tree.getLabelToIsReturnTrueMap();
		
		assertNotNull(sourceToTargetLabelMap);
		assertNotNull(targetJsonMap);
		assertNotNull(oldLabelToNewLabelMap);
		assertNotNull(labelToIsReturnTrueMap);
		
		System.out.println(oldLabelToNewLabelMap);
		
		//check the sizes of the maps after breaking down the simple query
		assertEquals(1, sourceToTargetLabelMap.size());
		assertEquals(1, targetJsonMap.size());
		assertEquals(3, labelToIsReturnTrueMap.size());
		assertEquals(1, oldLabelToNewLabelMap.size());
		
		
		
		tree=fragmentQuery.breakdownQuery(queryNested);
		//check the Label tree if query object is not empty
		assertNotNull(tree);
		
		//check the contents of Label tree 
		sourceToTargetLabelMap=tree.getSourceToTargetLabelMap();
		targetJsonMap=tree.getTargetJsonMap();
		oldLabelToNewLabelMap=tree.getOldLabelToNewLabelMap();
		labelToIsReturnTrueMap=tree.getLabelToIsReturnTrueMap();
		
		assertNotNull(sourceToTargetLabelMap);
		assertNotNull(targetJsonMap);
		assertNotNull(oldLabelToNewLabelMap);
		assertNotNull(labelToIsReturnTrueMap);
		
		System.out.println(oldLabelToNewLabelMap);
		
		//check the sizes of the maps after breaking down the Nested query
		assertEquals(2, sourceToTargetLabelMap.size());
		assertEquals(2, targetJsonMap.size());
		assertEquals(5, labelToIsReturnTrueMap.size());
		assertEquals(2, oldLabelToNewLabelMap.size());
	}

}
