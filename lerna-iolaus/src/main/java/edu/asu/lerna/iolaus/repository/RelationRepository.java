package edu.asu.lerna.iolaus.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.lerna.iolaus.domain.Relation;

/**
 * @author Lohith Dwaraka, Karan
 * 
 * Stores Node into the Neo4j through repository proxy  
 *
 */
public interface RelationRepository extends GraphRepository<Relation>{

	@Override
	@Transactional
	public <U extends Relation> U save(U arg0);
	
	

}
