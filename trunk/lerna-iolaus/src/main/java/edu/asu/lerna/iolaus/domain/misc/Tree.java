package edu.asu.lerna.iolaus.domain.misc;

import java.util.List;
import java.util.Map;

public class Tree {

	Map<String,List<List<String>>> sourceToTargetLabelMap;
	Map<String,String> targetJsonMap;
	Map<String,String> oldLabelToNewLabelMap;
	
	public Map<String, List<List<String>>> getSourceToTargetLabelMap() {
		return sourceToTargetLabelMap;
	}
	public void setSourceToTargetLabelMap(
			Map<String, List<List<String>>> sourceToTargetLabelMap) {
		this.sourceToTargetLabelMap = sourceToTargetLabelMap;
	}
	public Map<String, String> getTargetJsonMap() {
		return targetJsonMap;
	}
	public void setTargetJsonMap(Map<String, String> targetJsonMap) {
		this.targetJsonMap = targetJsonMap;
	}
	public Map<String, String> getOldLabelToNewLabelMap() {
		return oldLabelToNewLabelMap;
	}
	public void setOldLabelToNewLabelMap(Map<String, String> oldLabelToNewLabelMap) {
		this.oldLabelToNewLabelMap = oldLabelToNewLabelMap;
	}
	
	
}
