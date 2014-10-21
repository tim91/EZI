package put.poznan.EZI_Search.model;

import net.sf.extjwnl.data.IndexWord;

public class RelationshipScore implements Comparable<RelationshipScore> {

	public int compareTo(RelationshipScore o) {
		
		if(this.relationship > o.relationship)
			return -1;
		
		if(this.relationship < o.relationship)
			return 1;
		
		return 0;
		
	}

	public IndexWord source;
	public IndexWord target;
	public int relationship;
	
	@Override
	public String toString() {
	
		return "#### \n" + source.getLemma() + "\n" + target.getLemma() + "\nrelationship: " + relationship + "\n";
	}
}
