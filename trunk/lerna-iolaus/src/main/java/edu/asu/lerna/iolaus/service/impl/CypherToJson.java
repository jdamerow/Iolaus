package edu.asu.lerna.iolaus.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICypherToJson;
@Service
public class CypherToJson implements ICypherToJson {
	private static final Logger logger = LoggerFactory
			.getLogger(CypherToJson.class);
	@Override
	public String cypherToJson(String cypher){
		logger.info("***********************************\nCypher Query : "+cypher+"\n***********************************\n");
		String json="";
		String query="\"query\":";
		String regx="(\")([a-zA-Z0-9_.\\s?*()]*)(\")";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(cypher);
		Map<String,String> paramMap=new LinkedHashMap<String,String>();
		String p="param";
		int counter=0;
		String temp=cypher;
		int indexCount=0;
		if(cypher.contains("Where")){
			indexCount=cypher.substring(0, cypher.indexOf("Where")).split("\"").length/2;
		}else{
			indexCount=1;
		}
		int x=1;
		 while (matcher.find()) {
			 if(!paramMap.containsValue(matcher.group())){
				 counter++;
				 String replacement="{"+p+counter+"}";
				 String property=matcher.group();
				 if(x>indexCount)
					 paramMap.put("\""+p+counter+"\"","\"(?i).*"+property.substring(1,property.length()-1)+".*\"");
				 else
					 paramMap.put("\""+p+counter+"\"", property);
				 temp=temp.replaceAll(matcher.group(),replacement );
				 x++;
			 }
		 }
		 query+="\""+temp+"\"";
		 String params="\"params\":{";
		 for(Entry<String, String> entry:paramMap.entrySet()){
			 if(!params.equals("\"params\":{")){
				 params+=",";
			 }
			 params+=entry.getKey()+":"+entry.getValue();
		 }
		 params+="}";
		 json="{"+query+",\n"+params+"}";
		return json;
	}
}
