package put.poznan.EZI_Search.model;

public class DocScore implements Comparable<DocScore> {
    
	private double score;
    private int docId;

    public DocScore(double score, int docId) {
        this.score = score;
        this.docId = docId;
    }

    public int compareTo(DocScore docScore) {
        if (score > docScore.score) return -1;
        if (score < docScore.score) return 1;
        return 0;
    }

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}
    
    
    
}


