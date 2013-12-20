package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.iml.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jConfFile;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

@Service
public class Neo4jInstanceManager implements INeo4jInstanceManager {

	@Autowired Neo4jRegistry neo4jRegistry;
	
	private static final Logger logger = LoggerFactory
			.getLogger(Neo4jInstanceManager.class);
	@Override
	public String addNeo4jInstance(INeo4jInstance instance) {
		List<INeo4jConfFile> fileList=neo4jRegistry.getfileList();
		int maxId=1;
		for(INeo4jConfFile file:fileList){
			if(Integer.parseInt(file.getId())>maxId){
				maxId=Integer.parseInt(file.getId());
			}
		}
		String classPath=Neo4jInstanceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		BufferedWriter bw=null;
		try{
			bw=new BufferedWriter(new FileWriter(classPath.substring(0,classPath.indexOf("classes"))+"classes/Neo4jConfiguration"+maxId+1+".txt"));
			bw.append("id:"+maxId+1+"\n");
			bw.append("description:"+instance.getDescription()+"\n");
			bw.append("host:"+instance.getHost()+"\n");
			bw.append("ip:"+instance.getIp()+"\n"); 
			bw.append("port:"+instance.getPort());
			bw.close();
		}catch(IOException exception){
			if(bw==null){
				logger.info("Error occured while creating the file");
			}else{
				try{
				bw.close();
				logger.info("Error occured while writting to the file");
				}catch(IOException exp){
					System.out.println("Error occured while closing the file");
				}	
			}
		}
		return String.valueOf(maxId+1);
	}

	@Override
	public void deleteNeo4jInstance(String instanceId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<INeo4jInstance> listNeo4jInstances() {
		// TODO Auto-generated method stub
		return null;
	}

}
