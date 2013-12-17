package edu.asu.lerna.iolaus.domain.misc;

import java.util.Map;

import edu.asu.lerna.iolaus.domain.Label;

public class LabelTree {

	Map<String,Label> sourceToTargetLabelMap;
	Map<String,String> targetJsonMap;
	Map<String,String> oldLabelToNewLabelMap;
	Map<String,Boolean> labelToIsReturnMap;
	
	public Map<String, Boolean> getLabelToIsReturnMap() {
		return labelToIsReturnMap;
	}
	public void setLabelToIsReturnMap(Map<String, Boolean> labelToIsReturnMap) {
		this.labelToIsReturnMap = labelToIsReturnMap;
	}
	public Map<String, Label> getSourceToTargetLabelMap() {
		return sourceToTargetLabelMap;
	}
	public void setSourceToTargetLabelMap(
			Map<String, Label> sourceToTargetLabelMap) {
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
