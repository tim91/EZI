package put.poznan.EZI_Search.reader;

import java.util.Vector;

import org.junit.Test;

import put.poznan.EZI_Search.model.Document;

public class DocumentReaderTest {

	private String dirLocation = "./documents";
	
	@Test
	public void readDocuments(){
		
		DocumentReader dr = DocumentReader.getInstance();
		dr.setDocumentDirectory(dirLocation);
		
		Vector<Document> docs = dr.readDocuments();
		for (Document document : docs) {
			System.out.println(document.getTitile() + " " + document.getId());
			System.out.println(document.getContent());
			System.out.println("\n\n");
		}
	}
}
