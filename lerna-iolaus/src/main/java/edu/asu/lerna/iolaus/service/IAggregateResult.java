package edu.asu.lerna.iolaus.service;

import java.util.List;
import java.util.Map;

import edu.asu.lerna.iolaus.domain.Label;

public interface IAggregateResult {
	public void aggregateResults(
			Map<String, Map<String, List<Object>>> aggregatedResults,
			Map<String, List<Object>> processedResults,
			Map<String, Label> sourceToTargetLabelMap, Map<String, String> oldLabelToNewLabelMap, String sourceLabel); 
}
