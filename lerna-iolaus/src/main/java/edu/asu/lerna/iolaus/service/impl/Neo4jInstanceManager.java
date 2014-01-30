package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jInstance;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

/**
 * This class manages the Neo4jInstances. It has methods for adding, deleting, modifying Neo4j instances.
 * @author Karan Kothari
 *
 */
@Service
public class Neo4jInstanceManager implements INeo4jInstanceManager {

	@Autowired 
	Neo4jRegistry neo4jRegistry;

	private static final Logger logger = LoggerFactory
			.getLogger(Neo4jInstanceManager.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String addNeo4jInstance(INeo4jInstance instance) {
		if(instance!=null){
			List<INeo4jInstance> fileList=neo4jRegistry.getfileList();
			int maxId=0;
			String port=instance.getPort();
			String host=instance.getHost();
			if(port==null || host==null)
				return null;
			if(!checkConnectivity(port,host)&&instance.isActive()==true?true:false)
			{
				return "-2";
			}
			if(port.equals("")||host.equals(""))//if port and host is null then return "-1"
				return "-1";
			//This loop will search for the maximum id in the registry and will set it to the maxId
			for(INeo4jInstance file:fileList){
				if(file.getPort().equals(port)&&file.getHost().equalsIgnoreCase(host)){//if combination of port and host already exists then return "0"
					return "0";
				}
	
				if(Integer.parseInt(file.getId())>maxId){ 
					maxId=Integer.parseInt(file.getId());
				}
			}
			String classPath=Neo4jInstanceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();//gets the class path
			BufferedWriter bw=null;
			try{
				bw=new BufferedWriter(new FileWriter(URLDecoder.decode(classPath.substring(0,classPath.indexOf("classes")),"UTF-8")+"classes/ConfigurationFiles/Neo4jConfiguration"+(maxId+1)+".txt"));
				bw.append("id:"+(maxId+1)+"\n"); 
				String modifiedDescription=instance.getDescription().replaceAll("\n", " ").replace("\r","");
				bw.append("port:"+instance.getPort()+"\n");
				bw.append("host:"+instance.getHost()+"\n");
				bw.append("dbPath:"+instance.getDbPath()+"\n");
				bw.append("description:"+modifiedDescription+"\n");
				bw.append("active:"+String.valueOf(instance.isActive()==true?true:false)+"\n");
				bw.append("userName:"+instance.getUserName());
				bw.close();
				instance.setId(String.valueOf(maxId+1));
				neo4jRegistry.getfileList().add(instance);//add new instance to registry.
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
		}else{
			return null;
		}
	}
	
	/**
	 * This method ping the Neo4j server. If it is up then returns true else returns false.  
	 * @param port is port number.
	 * @param host is address of the host machine.
	 * @return the status of server. If it is up then returns true else returns false. 
	 */
	private boolean checkConnectivity(String port, String host) {
		boolean isAlive = true;
		String urlStr="";
		try {
			urlStr="http://"+host+":"+port+"/webadmin/";
			URL url = new URL(urlStr); 
			URLConnection connection= url.openConnection();//It will throw an exception if not able to connect to the server. 
			connection.getInputStream();
		} catch (Exception e) {
			isAlive = false;
			logger.error("Error in the connectivity : ",e);
		}
		return isAlive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteNeo4jInstance(String instanceId) throws UnsupportedEncodingException {
		if(instanceId!=null){
			List<INeo4jInstance> configFileList=neo4jRegistry.getfileList();
			for(INeo4jInstance configFile:configFileList){
				if(configFile.getId().equals(instanceId)){
					configFileList.remove(configFile);
					String classPath=Neo4jInstanceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
					String filePath=URLDecoder.decode(classPath.substring(0,classPath.indexOf("classes")),"UTF-8")+"classes/ConfigurationFiles/Neo4jConfiguration"+instanceId+".txt";
					File file = new File(filePath);
					file.delete();
					break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateNeo4jInstance(INeo4jInstance instance){
		List<INeo4jInstance> configFileList=neo4jRegistry.getfileList();
		String port=instance.getPort();
		String host=instance.getHost();
		if(!checkConnectivity(port, host)&&instance.isActive()==true?true:false){
			return 2;
		}
		for(INeo4jInstance configFile:configFileList){
			if(instance.getId().equals(configFile.getId())){
				String modifiedDescription=instance.getDescription().replaceAll("\n", " ").replace("\r","");
				configFile.setDescription(modifiedDescription);
				configFile.setHost(instance.getHost());
				configFile.setPort(instance.getPort());
				configFile.setActive(instance.isActive()==true?true:false);
			}
			else if(configFile.getPort().equals(port)&&configFile.getHost().equalsIgnoreCase(host)){//if combination of port and host already exists then return false
				return 1;
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public INeo4jInstance getInstance(String id){
		INeo4jInstance instance=null;
		for(INeo4jInstance confFile:neo4jRegistry.getfileList()){
			if(confFile.getId().equals(id)){
				instance=confFile;
				break;
			}
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<INeo4jInstance> getAllInstances() {
		return neo4jRegistry.getfileList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInstanceId(String port, String host){
		List<INeo4jInstance> fileList=neo4jRegistry.getfileList();
		for(INeo4jInstance file:fileList){
			if(file.getPort().equals(port)&&file.getHost().equalsIgnoreCase(host)){
				return file.getId();
			}
		}
		return null;
	}

}
