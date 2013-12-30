package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.iml.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jConfFile;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jConfFile;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jInstance;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

@Service
public class Neo4jInstanceManager implements INeo4jInstanceManager {

	@Autowired 
	Neo4jRegistry neo4jRegistry;
	
	private static final Logger logger = LoggerFactory
			.getLogger(Neo4jInstanceManager.class);
	@Override
	public String addNeo4jInstance(INeo4jInstance instance) {
		List<INeo4jConfFile> fileList=neo4jRegistry.getfileList();
		int maxId=0;
		String port=instance.getPort();
		String host=instance.getHost();
		for(INeo4jConfFile file:fileList){
			if(file.getPort().equals(port)&&file.getHost().equalsIgnoreCase(host)){
				return "0";
			}
			
			if(Integer.parseInt(file.getId())>maxId){
				maxId=Integer.parseInt(file.getId());
			}
		}
		String classPath=Neo4jInstanceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		BufferedWriter bw=null;
		try{
			bw=new BufferedWriter(new FileWriter(classPath.substring(0,classPath.indexOf("classes"))+"classes/ConfigurationFiles/Neo4jConfiguration"+(maxId+1)+".txt"));
			bw.append("id:"+(maxId+1)+"\n"); 
			String modifiedDescription=instance.getDescription().replaceAll("\n", " ").replace("\r","");
			bw.append("description:"+modifiedDescription+"\n");
			bw.append("host:"+instance.getHost()+"\n");
			bw.append("port:"+instance.getPort()+"\n");
			bw.append("active:"+String.valueOf(instance.isActive()==true?true:false));
			bw.close();
			INeo4jConfFile confFile=new Neo4jConfFile();
			confFile.setId(String.valueOf(maxId+1));
			confFile.setDescription(modifiedDescription);
			confFile.setHost(instance.getHost());
			confFile.setPort(instance.getPort());
			confFile.setActive(instance.isActive()==true?true:false);
			neo4jRegistry.getfileList().add(confFile);
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
	public boolean updateNeo4jInstance(INeo4jInstance instance){
		List<INeo4jConfFile> configFileList=neo4jRegistry.getfileList();
		String port=instance.getPort();
		String host=instance.getHost();
		for(INeo4jConfFile configFile:configFileList){
			if(instance.getId().equals(configFile.getId())){
				String modifiedDescription=instance.getDescription().replaceAll("\n", " ").replace("\r","");
				configFile.setDescription(modifiedDescription);
				configFile.setHost(instance.getHost());
				configFile.setPort(instance.getPort());
				configFile.setActive(instance.isActive()==true?true:false);
			}
			else if(configFile.getPort().equals(port)&&configFile.getHost().equalsIgnoreCase(host)){
				return false;
			}
		}
		return true;
	}
	
	@Override 
	public INeo4jInstance getInstance(String id){
		INeo4jInstance instance=new Neo4jInstance();
		for(INeo4jConfFile confFile:neo4jRegistry.getfileList()){
			if(confFile.getId().equals(id)){
				instance.setId(confFile.getId());
				instance.setPort(confFile.getPort());
				instance.setHost(confFile.getHost());
				instance.setDescription(confFile.getDescription());
				instance.setActive(confFile.isActive());
				break;
			}
		}
		return instance;
	}
	
	@Override
	public List<INeo4jInstance> getAllInstances() {
		List<INeo4jInstance> allInstances=new ArrayList<INeo4jInstance>();
		for(INeo4jConfFile instance:neo4jRegistry.getfileList()){
			INeo4jInstance castedInstance=new Neo4jInstance();
			castedInstance.setId(instance.getId());
			castedInstance.setPort(instance.getPort());
			castedInstance.setHost(instance.getHost());
			castedInstance.setDescription(instance.getDescription());
			castedInstance.setActive(instance.isActive());
			allInstances.add(castedInstance);
		}
		return allInstances;
	}

}
