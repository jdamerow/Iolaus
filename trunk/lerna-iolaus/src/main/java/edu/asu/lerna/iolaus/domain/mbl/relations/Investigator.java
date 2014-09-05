package edu.asu.lerna.iolaus.domain.mbl.relations;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Investigator extends Relation {
	
	private String type = "hasInvestigator";
	private String to;
	private Data data;

	public Investigator(String to, int year, String role, String subject) {
		data = new Data(year, role, subject);
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
		
		private long year;
		private String role;
		private String subject;
		private String dataset = "mblcourses";
		
		public Data(int year, String role, String subject) {
			this.year = year;
			this.role = role;
			this.subject = subject;
		}

		public String getDataset() {
			return dataset;
		}
		
		public long getYear() {
			return year;
		}

		public String getRole() {
			return role;
		}

		public String getSubject() {
			return subject;
		}

	}

}
