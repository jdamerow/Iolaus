package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;


@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlainQueryManagerTest {

	@Autowired
	private PlainQueryManager plainQueryManager;
	private StringBuilder xml;
	@Before
	public void setUp() throws Exception {
		String classPath = URLDecoder.decode(PlainQueryManagerTest.class
				.getProtectionDomain().getCodeSource().getLocation().getPath(),
				"UTF-8");
		BufferedReader br = new BufferedReader(new FileReader(
				classPath.substring(0, classPath.indexOf("classes"))
						+ "classes/plainQueryExample.xml"));
		String line="";
		xml=new StringBuilder();
		while((line=br.readLine())!=null){
			xml.append(line);
		}
		br.close();
	}

	@Test
	public void testExecuteQuery() throws JAXBException, SAXException, IOException {
		
		//valid input xml
		assertNotNull(plainQueryManager.executeQuery(xml.toString()));
		
		//null input
		assertNull(plainQueryManager.executeQuery(null));
		
		//input having only white spaces
		assertNull(plainQueryManager.executeQuery("   \n\r\t    \n\r\n\t"));
		
		boolean flag=false;
		//input which doesn't follow rules specified by the schema
		try{
			plainQueryManager.executeQuery("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}catch(SAXException exception){
			flag=true;
		}
		
		assertTrue(flag);
	}

}

