package edu.asu.lerna.iolaus.parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteChangesToFile {
	
	private static final Logger logger = LoggerFactory
			.getLogger(WriteChangesToFile.class);
	
	static Map<Integer,String> changes;
	public static void main(String[] args) throws IOException{
		BufferedReader firstFile=null,secondFile=null,allData=null;
		BufferedWriter finalData=null;
		try
		{
			firstFile=new BufferedReader(new FileReader("Changes.txt"));
			secondFile=new BufferedReader(new FileReader("ram_sim.txt"));
			allData=new BufferedReader(new FileReader("allDataUpdated.csv"));
			finalData=new BufferedWriter(new FileWriter("FinalData.csv"));
		}catch (IOException e) {
			logger.error("IO Error, File not found",e);
		}
		
		String line="";
		changes=new HashMap<Integer,String>();
		while((line=firstFile.readLine())!=null){
			int key=Integer.parseInt(line.substring(line.lastIndexOf(" ")+1));
			String value=line.substring(0,line.lastIndexOf(" "));
			if(!changes.containsKey(key)){
				changes.put(key, value);
			}
		}
		while((line=secondFile.readLine())!=null){
			int key=Integer.parseInt(line.substring(0,line.indexOf(" ")));
			String value=line.substring(line.indexOf(" ")+1);
			if(!changes.containsKey(key)){
				changes.put(key, value);
			}
		}
		firstFile.close();
		secondFile.close();
		
		int counter=1;
		String data="";
		while((data=allData.readLine())!=null){
			if(!changes.containsKey(counter)){
				finalData.append(data+"\n");
			}
			else{
				if(data.contains("Jr")){
					finalData.append(data+"\n");
				}
				else{
					String value=changes.get(counter);
					
					String col[]=data.split(",");
					if(value.contains(" ")){
						col[1]=value.substring(0,value.lastIndexOf(" "));
						col[2]=value.substring(value.lastIndexOf(" ")+1);
					}
					else
						col[1]=value;
					String l="";
					for(int i=0;i<col.length;i++){
						if(i==col.length-1)
							l=l+col[i];
						else
							l=l+col[i]+",";
					}
					finalData.append(l+"\n");
				}
			}
			counter++;
		}
		finalData.close();
		allData.close();
	}

}
