package put.poznan.EZI_Search.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import put.poznan.EZI_Search.model.Document;

public class DocumentReader extends AbstractReader {

	private static int counter = 1;
	
	private String documentDirectory;
	
	private String regexp = ".txt";
	
	private static DocumentReader docReader = null;
	
	public static DocumentReader getInstance(){
		
		if(docReader == null){
			docReader = new DocumentReader();
		}
		return docReader;
	}
	
	private DocumentReader(){}
	
	public TreeMap<Integer,Document> readDocuments(){
		
		TreeMap<Integer,Document> docs = new TreeMap<Integer,Document>();
		
		List<File> files = getFiles(documentDirectory, regexp);
		
		for (File file : files) {
			try {
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            Document d = null;
	            while (br.ready()) {
	                String line = br.readLine().trim().replaceAll("[^a-zA-Z ]", "").toLowerCase();;
	                if(line.length() == 0){
	                	//new doc
	                	docs.put(d.getId(),d);
	                	d = null;
	                	incrementCounter();
	                }else{
	                	if(d == null){
	                		d = new Document();
	                		d.setTitile(line);
	                		d.setId(counter);
	                		continue;
	                	}
	                	else{
	                		d.addLineContent(line);
	                	}
	                }
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("File not found");
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		return docs;
	}
	
	private void incrementCounter(){
		counter++;
	}

	public String getDocumentDirectory() {
		return documentDirectory;
	}

	public void setDocumentDirectory(String documentDirectory) {
		this.documentDirectory = documentDirectory;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	
}
