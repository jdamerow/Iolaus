package edu.asu.lerna.iolaus.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICypherToJson;

/**
 * This class converts a cypher query into a json query.
 * @author Karan Kothari
 *
 */
@Service
public class CypherToJson implements ICypherToJson {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String cypherToJson(String cypher){
		String json="";
		String query="\"query\":";
		String regex="(~*)(\")([a-zA-Z0-9_.\\s?*()]*)(\")";//Regular expression for extracting strings in ""
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cypher);
		Map<String,String> paramMap=new LinkedHashMap<String,String>();
		String p="param";
		int counter=0;
		String temp=cypher;
		int indexCount=0;
		if(cypher.contains("Where")){//if cypher query has where clause 
			indexCount=cypher.substring(0, cypher.indexOf("Where")).split("\"").length/2;//scan for " in where clause and count the number of strings in ""
		}else{
			indexCount=1;
		}
		int x=1;
		 while (matcher.find()) {
			 if(!paramMap.containsValue(matcher.group())){
				 counter++;
				 String replacement="{"+p+counter+"}";
				 String property=matcher.group();
				 if(x>indexCount){// If property in where clause
					 if(property.charAt(0)=='~'){
						 paramMap.put("\""+p+counter+"\"", "\"(?i).*"+property.substring(2,property.length()-1)+".*\"");
						 temp=temp.replaceAll(property.substring(1),replacement );
					 }else{
						 paramMap.put("\""+p+counter+"\"",property);
						 temp=temp.replaceAll(matcher.group(),replacement );
					 }
				 }
				 else {//If it is property in Start clause
					 paramMap.put("\""+p+counter+"\"", property);
					 temp=temp.replaceAll(matcher.group(),replacement );
				 }
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String plainQueryToJson(String cypher){
		cypher=eliminateWhiteSpaces(cypher);
		System.out.println(cypher);
		String json="";
		String query="\"query\":";
		String regex="(\")([a-zA-Z0-9_.\\s?*()]*)(\")";//Regular expression for extracting strings in ""
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cypher);
		Map<String,String> paramMap=new LinkedHashMap<String,String>();
		String p="param";
		int counter=0;
		String temp=cypher;
		/*replaces all  values "" with param from the keys of "params" */
		while (matcher.find()) {
			 if(!paramMap.containsValue(matcher.group())){
				 counter++;
				 String replacement="{"+p+counter+"}";
				 String property=matcher.group();
				 paramMap.put("\""+p+counter+"\"","\""+property.substring(1,property.length()-1)+"\"");
				 temp=temp.replaceAll(matcher.group(),replacement );
			 }
		 }
		 /*creates adds keys and values in the "params" of the json*/
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

	private String eliminateWhiteSpaces(String cypher) {
		cypher=cypher.replaceAll("\\s+", " ");
		return cypher;
	}
}
