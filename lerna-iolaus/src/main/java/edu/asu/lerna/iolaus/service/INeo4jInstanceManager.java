package edu.asu.lerna.iolaus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;


public interface INeo4jInstanceManager {
	
	abstract String addNeo4jInstance(INeo4jInstance instance);
	abstract void deleteNeo4jInstance(String instanceId);
	abstract List<INeo4jInstance> listNeo4jInstances();
}
