package edu.asu.lerna.iolaus.domain.mbl.relations;


public class IsPartOf {
	
	private String type = "isPartOf";
	private String to;
	private Data data;
	
	public IsPartOf(String to, int year) {
		data = new Data(year);
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
	
	public class Data {
		
		private int year;
		private String dataset = "mblcourses";
		
		public Data(int year) {
			this.year = year;
		}

		public String getDataset() {
			return dataset;
		}
		
		public int getYear() {
			return year;
		}

	}

}
