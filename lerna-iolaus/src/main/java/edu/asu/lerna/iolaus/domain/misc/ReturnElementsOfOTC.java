package edu.asu.lerna.iolaus.domain.misc;
import java.util.Map;

public class ReturnElementsOfOTC {
	Map<Object,String> objectToTargetLabelMap;
	String json;
	
	public Map<Object, String> getObjectToTargetLabelMap() {
		return objectToTargetLabelMap;
	}
	public void setObjectToTargetLabelMap(Map<Object, String> objectToTargetLabelMap) {
		this.objectToTargetLabelMap = objectToTargetLabelMap;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
}
