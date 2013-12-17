package edu.asu.lerna.iolaus.domain.misc;

import java.util.List;
import java.util.Map;

public class ResultSet {
	
	Map<String,List<Object>> resultSet;
	Map<String,Boolean> labelToIsReturnTrueMap;
	
	public Map<String, List<Object>> getResultSet() {
		return resultSet;
	}
	public void setResultSet(Map<String, List<Object>> resultSet) {
		this.resultSet = resultSet;
	}
	public Map<String, Boolean> getLabelToIsReturnTrueMap() {
		return labelToIsReturnTrueMap;
	}
	public void setLabelToIsReturnTrueMap(Map<String, Boolean> labelToIsReturnMap) {
		this.labelToIsReturnTrueMap = labelToIsReturnMap;
	}
}
