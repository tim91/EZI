package put.poznan.EZI_Search.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import put.poznan.EZI_Search.model.Document;


public class KeywordsReader extends AbstractReader {


	private String keywordDirectory;
	
	private String regexp = "txt";
	
	public Vector<String> getKeywords(){
		
		Vector<String> keywords = new Vector<String>(10);
		
		List<File> files = getFiles(keywordDirectory, regexp);
		
		for (File file : files) {
			
			try {
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            while (br.ready()) {
	                String keyword = br.readLine().trim();
	                if(keyword.length() > 0){
	                	keywords.add(keyword);
	                }
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("No database available.");
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		
		return keywords;
	}
}
