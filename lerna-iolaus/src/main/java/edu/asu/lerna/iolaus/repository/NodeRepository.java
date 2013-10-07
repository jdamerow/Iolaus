package edu.asu.lerna.iolaus.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.lerna.iolaus.domain.Node;

public interface NodeRepository extends GraphRepository<Node>{

	@Override
	@Transactional
	public <U extends Node> U save(U arg0);
}
