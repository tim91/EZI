package put.poznan.EZI_Search.model;

public class ExtendedQuery {

	public String query;
	public int summaryRelatioship = 0;
	
	@Override
	public String toString() {
		
		return query + " " + summaryRelatioship;
	}
}
