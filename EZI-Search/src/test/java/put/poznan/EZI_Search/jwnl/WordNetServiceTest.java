package put.poznan.EZI_Search.jwnl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.relationship.RelationshipFinder;

import org.junit.Test;

import put.poznan.EZI_Search.model.ExtendedQuery;
import put.poznan.EZI_Search.model.Query;
import put.poznan.EZI_Search.model.RelationshipScore;

public class WordNetServiceTest {

	public List<RelationshipScore> getSynonyms(String lemma){
		
		List<RelationshipScore> relationship = new ArrayList<RelationshipScore>();
		
		WordNetService wns = WordNetService.getInstance();
		
		IndexWordSet indexWords = wns.getIndexWords(lemma);
		
		for (IndexWord word : indexWords.getIndexWordCollection()) {
			Set<IndexWord> set = wns.getSynonyms(word);
//			System.out.println("Index Word: " + word);
//			for(Synset s : word.getSenses()){
//				System.out.println(s);
//			}
			
			for (IndexWord synonym : set) {
				try {
					int rel = RelationshipFinder.getImmediateRelationship(word, synonym);
					RelationshipScore rs = new RelationshipScore();
					rs.relationship = rel;
					rs.source = word;
					rs.target = synonym;
					relationship.add(rs);
				} catch (Exception e) {
				}
			}
			
		}
		
		Collections.sort(relationship);
		
		return relationship;
	}
	
	@Test
	public void extendQuery(){
		String q = "artificial intelligence";
		
		List<ExtendedQuery> list = getBestSynonymQueries(new Query(q), 10);
		for (ExtendedQuery query : list) {
			System.out.println(query);
		}
	}
	
	public List<ExtendedQuery> getBestSynonymQueries(Query q, int best){
		
		String[] elems = q.getQuery().split(" ");
		
		List<ExtendedQuery> extended = new ArrayList<ExtendedQuery>(best);
		
		for(int j=0; j< best; j++){
			ExtendedQuery eq = new ExtendedQuery();
			eq.query = q.getQuery();
			extended.add(eq);
		}
		
		for (String elem : elems) {
			
			List<RelationshipScore> rels = getSynonyms(elem);
//			System.out.println(rels);
			
			int iters = best > rels.size() ? rels.size() : best;
			
			for(int i=0 ; i< iters; i++){
				RelationshipScore rs = rels.get(i);
				//extend query
				ExtendedQuery eq = extended.get(i);
				
				eq.query += " " + rs.target.getLemma();
				eq.summaryRelatioship += rs.relationship;
				
			}
		}
		
		return extended;
		
	}
	
}
