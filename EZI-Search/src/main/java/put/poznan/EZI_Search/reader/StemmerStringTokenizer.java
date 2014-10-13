package put.poznan.EZI_Search.reader;

import java.util.StringTokenizer;

import put.poznan.EZI_Search.Stemmer.Stemmer;

public class StemmerStringTokenizer extends StringTokenizer {

	private Stemmer stemmer;
	
	public StemmerStringTokenizer(String str, String s) {
		super(str,s);
		
		stemmer = new Stemmer();
	}

	@Override
	public String nextToken() {
		
		String token = super.nextToken();
		stemmer.add(token.toCharArray(),token.length());
		stemmer.stem();
		return stemmer.toString();
	}
	
}
