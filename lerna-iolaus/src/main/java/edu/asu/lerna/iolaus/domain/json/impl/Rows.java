package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rows {
	private List<Row> row;
	
	
	public void addRow(Row row) {
		if(this.row == null) {
			this.row = new ArrayList<Row>();
		}
		
		this.row.add(row);
	}


	public List<Row> getRow() {
		return row;
	}


	public void setRow(List<Row> row) {
		this.row = row;
	}
}
