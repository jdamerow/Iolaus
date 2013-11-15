package edu.asu.lerna.iolaus.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICypherToJson;
@Service
public class CypherToJson implements ICypherToJson {
	@Override
	public String cypherToJson(String cypher){
		String json="";
		/*{
			  "query" : "START x  = node(353) MATCH x -[r]-> n RETURN type(r), n.name, n.age",
			  "params" : {
			  }
			}*/
		String query="\"query\":"+"\"";
		String regx="(\")([a-zA-Z0-9_.]*)(\")";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(cypher);
		Map<String,String> paramMap=new LinkedHashMap<String,String>();
		String p="param";
		int counter=0;
		String temp=cypher;
		 while (matcher.find()) {
			 if(!paramMap.containsValue(matcher.group())){
				 counter++;
				 String replacement="{"+p+counter+"}";
				 paramMap.put("\""+p+counter+"\"", matcher.group());
				 temp=temp.replaceAll(matcher.group(),replacement );
			 }
		 }
		 query="\""+temp+"\"";
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
