package put.poznan.EZI_Search.model;

import java.util.StringTokenizer;

import put.poznan.EZI_Search.reader.StemmerStringTokenizer;

public class Query {

	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Query(String query) {
		super();
		this.query = query;
	}
	
	public StringTokenizer tokenize(){
		return new StemmerStringTokenizer(this.query, " ");
	}
	
	@Override
	public String toString() {
		return this.query;
	}
}
