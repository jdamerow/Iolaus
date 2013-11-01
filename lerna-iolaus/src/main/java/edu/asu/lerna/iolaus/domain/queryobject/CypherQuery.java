package edu.asu.lerna.iolaus.domain.queryobject;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CypherQuery {
	public static String start="Start ";
	public static String match="match ";
	public static String where="where ";
	public static String ret="return ";
	public static Map<PropertyOf,String> nodeRel=new HashMap<PropertyOf,String>();
	public static Stack<String> operator = new Stack<String>();
	public static String currentOperator="";
	
	public String getCypher(){
		String cypher=start;
		if(!match.equals("match ")){
			cypher=cypher+match;
		}
		if(!where.equals("where ")){
			cypher=cypher+where;
		}
		cypher=cypher+ret;
		return cypher;
	}
}
