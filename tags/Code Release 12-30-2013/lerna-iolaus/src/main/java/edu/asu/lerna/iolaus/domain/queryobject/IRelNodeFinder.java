package edu.asu.lerna.iolaus.domain.queryobject;



public interface IRelNodeFinder {

	public abstract IRelNodeFinderData getNodeRel(INode node,
			IRelNodeFinderData rnfd);

	public abstract IRelNodeFinderData parseOperatorRel(IOperator op,
			IRelNodeFinderData rnfd);

	public abstract IRelNodeFinderData parseRelNodeRel(IRelNode relNode,
			IRelNodeFinderData rnfd);

	public abstract IRelNodeFinderData getRelationDetailsRel(
			IRelationship relationship, IRelNodeFinderData rnfd);


}