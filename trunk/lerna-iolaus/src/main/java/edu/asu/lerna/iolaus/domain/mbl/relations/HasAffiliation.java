package edu.asu.lerna.iolaus.domain.mbl.relations;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class HasAffiliation {
	
	private String type = "hasAffiliation";
	private String to;
	private Data data;

	public HasAffiliation(String to, int year, String position) {
		data = new Data(year, position);
		this.to = to;
	}
	
	public String getType() {
		return type;
	}
	
	public String getTo() {
		return to;
	}
	
	public Data getData() {
		return data;
	}
	
	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(this);
		return json;
	}

	public class Data {
		private int year;
		private String position;
		private String dataset = "mblcourses";
		
		public Data(int year, String position) {
			this.year = year;
			this.position = position;
		}

		public String getDataset() {
			return dataset;
		}
		
		public int getYear() {
			return year;
		}

		public String getPosition() {
			return position;
		}

	}

}
