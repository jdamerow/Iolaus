package edu.asu.lerna.iolaus.repository;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;

public interface NodeRepository extends GraphRepository<Node>{

	Node findById(long id);
	
	@Override
	public EndResult<Node> findAllByPropertyValue(String property, Object value);
	
	@Override
	@Transactional
	public <U extends Node> U save(U arg0);
}
