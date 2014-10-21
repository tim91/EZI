package put.poznan.EZI_Search.jwnl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordNetService {

	private String dictionaryPath = "wordNetDictionary/map_properties.xml";

	private Dictionary dic = null;

	public static WordNetService service = null;

	public static WordNetService getInstance() {
		if (service == null) {
			service = new WordNetService();
		}
		return service;
	}

	public IndexWord getIndexWord(POS pos, String word) {
		try {
			return dic.lookupIndexWord(pos, word);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private WordNetService() {

		try {
			dic = Dictionary.getDefaultResourceInstance();// Dictionary.getInstance(new
															// FileInputStream(dictionaryPath));
		}
		// catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IndexWordSet getWordSet(String word) {

		try {
			// IndexWord iw = dic.getIndexWord(p, word);
			IndexWordSet w = dic.lookupAllIndexWords(word);
			return w;

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public IndexWordSet getIndexWords(String word) {
		try {
			return dic.lookupAllIndexWords(word);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Set<String> getSynonyms(String word) {

		Set<String> set = new HashSet<String>();

		IndexWordSet w;
		try {
			w = dic.lookupAllIndexWords(word);

			for (IndexWord wordInIndex : w.getIndexWordArray()) {

				for (Synset sense : wordInIndex.getSenses()) {

					PointerTargetNodeList synonyms = PointerUtils
							.getSynonyms(sense);
					synonyms.print();
					// for (PointerTargetNode synonym : synonyms) {
					// if(synonym.isLexical()){
					// System.out.println("Word: " + synonym.getWord());
					// set.add(synonym.getWord().getLemma());
					// }
					// else{
					// System.out.println(synonym);
					// for(Pointer p : sense.getPointers()){
					// System.out.println(p);
					// }
					//
					// }
					// }

				}

			}

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return set;
	}

	public Set<IndexWord> getSynonyms(IndexWord word) {

		Set<IndexWord> set = new HashSet<IndexWord>();

		try {
			IndexWord w = dic.lookupIndexWord(word.getPOS(), word.getLemma());

			for (Synset s : w.getSenses()) {
				
				for(Word word2 : s.getWords()){
					set.add(new IndexWord(dic, word2.getLemma(), word2
					.getPOS(), word2.getSynset()));
				}
			}
			
//			for (Synset sense : w.getSenses()) {
//
//				PointerTargetNodeList synonyms = PointerUtils
//						.getSynonyms(sense);
//
//				for (PointerTargetNode synonym : synonyms) {
//
//					List<Word> words = synonym.getSynset().getWords();
//
//					for (Word word2 : words) {
//						set.add(new IndexWord(dic, word2.getLemma(), word2
//								.getPOS(), word2.getSynset()));
//					}
//				}
//
//			}

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return set;
	}

	public void getHyponyms(IndexWord iw) {
		try {
			// PointerTargetNodeList res =
			// PointerUtils.getDirectHypernyms(iw.getSenses().get(0));

			// od ogolnego do bardziej szczegolowego
			// PointerTargetNodeList res = PointerUtils.getDirectHyponyms(iw
			// .getSenses().get(0));
			//
			// Iterator<PointerTargetNode> it = res.iterator();
			//
			// while (it.hasNext()) {
			// Synset s = it.next().getSynset();
			// System.out.println(s);
			// }
			//
			// System.out.println("--------------------");
			//
			// // Od szczegolowego do ogolnego
			// PointerTargetNodeList res1 = PointerUtils.getDirectHypernyms(iw
			// .getSenses().get(0));
			// it = res1.iterator();
			//
			// while (it.hasNext()) {
			// Synset s = it.next().getSynset();
			// System.out.println(s);
			// }
			//
			System.out.println("synonyms");

			for (Synset ss : iw.getSenses()) {
				PointerTargetNodeList res2 = PointerUtils.getSynonyms(ss);

				Iterator<PointerTargetNode> it = res2.iterator();
				while (it.hasNext()) {
					System.out.println(it.next());
				}
			}

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void demonstrateSymmetricRelationshipOperation(IndexWord start,
			IndexWord end) throws JWNLException, CloneNotSupportedException {
		// find all synonyms that <var>start</var> and <var>end</var> have in
		// common
		RelationshipList list = RelationshipFinder.findRelationships(start
				.getSenses().get(0), end.getSenses().get(0),
				PointerType.SIMILAR_TO);
		System.out.println("Synonym relationship between \"" + start.getLemma()
				+ "\" and \"" + end.getLemma() + "\":");
		for (Object aList : list) {
			((Relationship) aList).getNodeList().print();
		}
		System.out.println("Depth: " + list.get(0).getDepth());
	}

	public void demonstrateAsymmetricRelationshipOperation(IndexWord start,
			IndexWord end) throws JWNLException, CloneNotSupportedException {
		// Try to find a relationship between the first sense of
		// <var>start</var> and the first sense of <var>end</var>
		RelationshipList list = RelationshipFinder.findRelationships(start
				.getSenses().get(0), end.getSenses().get(0),
				PointerType.HYPERNYM);
		System.out.println("Hypernym relationship between \""
				+ start.getLemma() + "\" and \"" + end.getLemma() + "\":");
		for (Object aList : list) {
			((Relationship) aList).getNodeList().print();
		}
		System.out
				.println("Common Parent Index: "
						+ ((AsymmetricRelationship) list.get(0))
								.getCommonParentIndex());
		System.out.println("Depth: " + list.get(0).getDepth());
	}
}
