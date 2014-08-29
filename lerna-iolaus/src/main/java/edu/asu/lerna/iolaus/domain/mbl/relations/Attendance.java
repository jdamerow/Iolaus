package edu.asu.lerna.iolaus.domain.mbl.relations;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Attendance extends Relation {

	private String type = "attendance";
	private String to;
	private Data data;
	
	public Attendance(String to, int year, String role) {
		data = new Data(year, role);
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
		private String role;
		private String dataset = "mblcourses";
		
		public Data(int year, String role) {
			this.year = year;
			this.role = role;
		}
	
		public String getDataset() {
			return dataset;
		}
		
		public int getYear() {
			return year;
		}
	
	
		public String getRole() {
			return role;
		}

	}
}
