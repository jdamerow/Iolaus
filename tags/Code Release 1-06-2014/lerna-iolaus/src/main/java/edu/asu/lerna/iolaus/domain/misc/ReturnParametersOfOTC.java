package edu.asu.lerna.iolaus.domain.misc;
import java.util.Map;

public class ReturnParametersOfOTC {
	Map<Object,String> objectToTargetLabelMap;
	Map<String,Boolean> isReturnMap;
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
	public Map<String, Boolean> getIsReturnMap() {
		return isReturnMap;
	}
	public void setIsReturnMap(Map<String, Boolean> labelToIsReturnMap) {
		this.isReturnMap = labelToIsReturnMap;
	}
	
}
