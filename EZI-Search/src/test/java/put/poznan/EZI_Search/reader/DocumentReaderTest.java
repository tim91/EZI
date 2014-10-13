package put.poznan.EZI_Search.reader;

import java.util.TreeMap;
import java.util.Vector;

import org.junit.Test;

import put.poznan.EZI_Search.model.Document;

public class DocumentReaderTest {

	private String fileLocation = "./documents/docs.txt";
	
	@Test
	public void readDocuments(){
		
		DocumentReader dr = DocumentReader.getInstance();
		dr.setDocumentFile(fileLocation);
		TreeMap<Integer, Document> docs = dr.readDocumentsFromFile();
	
		for (Document document : docs.values()) {
			System.out.println(document.getTitile() + " " + document.getId());
			System.out.println(document.getContent());
			System.out.println("\n\n");
		}
	}
}
