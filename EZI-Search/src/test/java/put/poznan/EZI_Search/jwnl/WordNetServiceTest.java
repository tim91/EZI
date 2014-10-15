package put.poznan.EZI_Search.jwnl;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;

import org.junit.Test;

public class WordNetServiceTest {

	@Test
	public void readWord(){
		
		WordNetService wns = WordNetService.getInstance();
		
		IndexWordSet iws = wns.getWordSet("elephant");
		
		for (IndexWord iw : iws.getIndexWordArray()) {
			System.out.println(iw);
			
			wns.getHyponyms(iw);
		}
	}
	
}
