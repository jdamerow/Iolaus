package edu.asu.lerna.iolaus.domain.dataset;

public interface IProperty {

	String getName();

	void setName(String value);

	String getValue();

	void setValue(String value);

	/**
	 * This method generate the json body for adding property to the node index
	 * @param nodeURI is id of the node corresponding to this property.
	 * @return json body.
	 * 
	 * e.g. {
	    	  "value" : "some value",
	    	  "uri" : "http://localhost:7474/db/data/node/83",
	    	  "key" : "some-key"
    		}
	 */
	String getJsonProperty(String nodeURI);

}
