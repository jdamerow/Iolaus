package edu.asu.lerna.iolaus.domain.json.impl;



public class Data{
   	private String __type__;
   	private String dataset;
   	private String firstName;
   	private String label;
   	private String lastName;
   	private String serviceId;
   	private String type;
   	private String uri;

 	public String get__type__(){
		return this.__type__;
	}
	public void set__type__(String __type__){
		this.__type__ = __type__;
	}
 	public String getDataset(){
		return this.dataset;
	}
	public void setDataset(String dataset){
		this.dataset = dataset;
	}
 	public String getFirstName(){
		return this.firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
 	public String getLabel(){
		return this.label;
	}
	public void setLabel(String label){
		this.label = label;
	}
 	public String getLastName(){
		return this.lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
 	public String getServiceId(){
		return this.serviceId;
	}
	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public String getUri(){
		return this.uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
}
