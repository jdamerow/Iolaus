package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.exception.UploadDatasetException;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UploadManagerTest {
	@Autowired
	private UploadManager uploadManager;
	private StringBuilder datasetXml;
	@Before
	public void setUp() throws Exception {
		String classPath = URLDecoder.decode(UploadManagerTest.class
				.getProtectionDomain().getCodeSource().getLocation().getPath(),
				"UTF-8");
		BufferedReader br = new BufferedReader(new FileReader(
				classPath.substring(0, classPath.indexOf("classes"))
						+ "classes/sampleDataset.xml"));
		String line="";
		datasetXml=new StringBuilder();
		while((line=br.readLine())!=null){
			datasetXml.append(line);
		}
		br.close();
	}

	@Test
	public void testUploadDataset() throws UploadDatasetException {
		try {
			boolean result=uploadManager.uploadDataset(datasetXml.toString());
			assertEquals(true, result);
			assertEquals(false, uploadManager.uploadDataset(null));
			assertEquals(false, uploadManager.uploadDataset(""));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
