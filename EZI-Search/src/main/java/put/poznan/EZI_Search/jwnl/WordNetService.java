package put.poznan.EZI_Search.jwnl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordNetService {

	private String dictionaryPath = "wordNetDictionary/map_properties.xml";
	
	private Dictionary dic = null;
	
	public static WordNetService service = null;
	
	public static WordNetService getInstance(){
		if(service == null){
			service = new WordNetService();
		}
		return service;
	}
	
	private WordNetService() {
		
		try {
			dic = Dictionary.getDefaultResourceInstance();//Dictionary.getInstance(new FileInputStream(dictionaryPath));
		} 
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IndexWordSet getWordSet(String word){
		
		try {
//			IndexWord iw = dic.getIndexWord(p, word);
			IndexWordSet w = dic.lookupAllIndexWords(word);
			return w;
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void getHyponyms(IndexWord  iw){
		try {
			PointerTargetNodeList res = PointerUtils.getDirectHypernyms(iw.getSenses().get(0));
			
			System.out.println(res);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
