package put.poznan.EZI_Search.jwnl;

import java.util.List;

import org.junit.Test;

import put.poznan.EZI_Search.model.ExtendedQuery;

public class WordNetServiceTest {

	
	@Test
	public void getExtendQuery(){
		String q = "artificial intelligence";
		
		WordNetService wns = WordNetService.getInstance();
		
		List<ExtendedQuery> list = wns.getBestSynonymQueries(q, 10);
		for (ExtendedQuery query : list) {
			System.out.println(query);
		}
	}
	
	
}
