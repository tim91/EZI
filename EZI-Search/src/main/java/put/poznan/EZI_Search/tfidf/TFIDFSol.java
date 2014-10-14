package put.poznan.EZI_Search.tfidf;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import put.poznan.EZI_Search.model.DocScore;
import put.poznan.EZI_Search.model.Document;
import put.poznan.EZI_Search.model.Query;
import put.poznan.EZI_Search.model.SearchReport;
import put.poznan.EZI_Search.reader.DocumentReader;
import put.poznan.EZI_Search.reader.KeywordsReader;
import put.poznan.EZI_Search.reader.StemmerStringTokenizer;

public class TFIDFSol {
	
	TreeMap<Integer,Document> db = new TreeMap<Integer,Document>(); // the document collection
    TreeMap<String, Double> idfs = new TreeMap<String, Double>(); // idf value for each term in the vocabulary
    Vector<String> keywords = new Vector<String>();
    TreeMap<String, Set<Integer>> invertedFile = new TreeMap<String, Set<Integer>>(); // term -> docIds of docs containing the term
    Vector<TreeMap<String, Double>> tf = new Vector<TreeMap<String, Double>>(); // term x docId matrix with term frequencies

    private String documentsFile;
    private String keywordsFile;
    private Query query;
    
    private static TFIDFSol sol;
    
    private TFIDFSol (){}
    
    public static TFIDFSol getInstance(){
    	if(sol == null){
    		sol = new TFIDFSol();
    	}
    	return sol;
    }

    public SearchReport search() {
        // init the database
        initDB(this.documentsFile);

        initKeywords(this.keywordsFile);
        
        // init global variables: tf, invertedFile, and idfs
        init();

        // print the database
//        printDB();

        // idfs and tfs
        System.out.println("IDFs:");
        // print the vocabulary
        printVoc();
    
        // similarities for different queries
        Vector<DocScore> scores = rank(this.query);
        
        SearchReport r = new SearchReport();
        for (DocScore docScore : scores) {
            r.addReportLine(db.get(docScore.getDocId()).getTitile() + "; " + docScore.getScore());
        }
        
        return r;
    }

    private void initKeywords(String keywordsFile) {
		// TODO Auto-generated method stub

    	//read keywords
    	KeywordsReader kr = KeywordsReader.getInstance();
    	kr.setKeywordFile(keywordsFile);
    	keywords = kr.readKeywordsFromFile();
    	tokenizeKeywords();
	}

    private void tokenizeKeywords()
    {
        Vector<String> tokenizedKeywords=new Vector<String>();
    	for ( String keyword : keywords )
        {
            String tokenizedKeyword=new StemmerStringTokenizer( keyword, " " ).nextToken();
            tokenizedKeywords.add( tokenizedKeyword );
        }
    	keywords=tokenizedKeywords;
    }

	// inits database from textfile
    private void initDB(String documentFile) {
        DocumentReader reader = DocumentReader.getInstance();
        reader.setDocumentFile(this.documentsFile);
        db = reader.readDocumentsFromFile();
    }

    // lists the vocabulary
    private void printVoc() {
        System.out.println("Vocabulary:");
        for (Map.Entry<String, Double> entry : idfs.entrySet()) {
            System.out.println(entry.getKey() + ", idf = " + entry.getValue());
        }
    }

    // lists the database
    private void printDB() {
        System.out.println("size of the database: " + db.size());
        
        for (Document doc : db.values()) {
        	System.out.println("doc " + doc.getId() + ": " + doc.getTitile());
		}
        System.out.println("");
    }

    // calculates the similarity between two vectors
    // each vector is a term -> weight map
    public double similarity(TreeMap<String, Double> v1, TreeMap<String, Double> v2) {
        double sum = 0;
        // iterate through one vector
        for (Map.Entry<String, Double> entry : v1.entrySet()) {
            String term = entry.getKey();
            Double w1 = entry.getValue();
            // multiply weights if contained in second vector
            Double w2 = v2.get(term);
            if (w2 != null)            	
                sum += w1 * w2;
        }
        // TODO write the formula for computation of cosinus
        // note that v.values() is Collection<Double> that you may need to calculate length of the vector
        // take advantage of vecLength() function
        
        double sim = sum / (vecLength(v1.values())*vecLength(v2.values()));
        
        return sim;
    }

    // returns the length of a vector
    private double vecLength(Collection<Double> vec) {
        double sum = 0;
        for (Double d : vec) {
            sum += Math.pow(d, 2);
        }
        return Math.sqrt(sum);
    }

    // ranks a query to the documents of the database
    private Vector<DocScore> rank(Query query) {
        System.out.println("");
        System.out.println("query = " + query);

        // get term frequencies for the query terms
        TreeMap<String, Double> termFreqs = getTF(query.tokenize());

        // construct the query vector
        // the query vector
        TreeMap<String, Double> queryVec = new TreeMap<String, Double>();

        // iterate through all query terms
        for (Map.Entry<String, Double> entry : termFreqs.entrySet()) {
            String term = entry.getKey();
            //TODO compute tfidf value for terms of query
            
            double idf = idf(term);
            
            double tfidf = idf * entry.getValue();
            queryVec.put(term, tfidf);
        }

        Set<Integer> union;
        TreeSet<String> queryTerms = new TreeSet<String>(termFreqs.keySet());

        // from the inverted file get the union of all docIDs that contain any query term
        union = invertedFile.get(queryTerms.first());
        for (String term : queryTerms) {
            union.addAll(invertedFile.get(term));
        }

        // calculate the scores of documents in the union
        Vector<DocScore> scores = new Vector<DocScore>();
        for (Integer i : union) {
            scores.add(new DocScore(similarity(queryVec, getDocVec(i)), i));
        }

        
        // sort and print the scores
        Collections.sort(scores);
        
        return scores;
    }

    // returns the idf of a term
    private double idf(String term) {
    	if(term == null)
    		System.out.println();
        return idfs.get(term);
    }

    // calculates the document vector for a given docID
    private TreeMap<String, Double> getDocVec(int docId) {
        TreeMap<String, Double> vec = new TreeMap<String, Double>();

        // get all term frequencies
        TreeMap<String, Double> termFreqs = tf.elementAt(docId);

        // for each term, tf * idf
        for (Map.Entry<String, Double> entry : termFreqs.entrySet()) {
            String term = entry.getKey();
            //TODO compute tfidf value for a given term
            //take advantage of idf() function
            double tfidf = entry.getValue() * idf(term);
            vec.put(term, tfidf);
        }
        return vec;
    }

    // returns the term frequency for a term and a docID
    private double getTF(String term, int docId) {
        Double freq = tf.elementAt(docId).get(term);
        if (freq == null) return 0;
        else return freq;
    }

    
    // calculates the term frequencies for a document
    private TreeMap<String, Double> getTF(StringTokenizer tokenizedDoc) {
    	
        TreeMap<String, Double> termFreqs = new TreeMap<String, Double>();
        double max = 0;

        // tokenize document
        StringTokenizer st = tokenizedDoc;

        // for all tokens
        while (st.hasMoreTokens()) {
            String term = st.nextToken();

            // count the max term frequency
            Double count = termFreqs.get(term);
            if (count == null) {
                count = new Double(0);
            }
            
            //check if term exits in keywords vector
            if(this.keywords.contains(term)){
            	count++;
            	termFreqs.put(term, count);
                if (count > max) max = count;
            }
            
            
        }

        // normalize tf
        for (String key : termFreqs.keySet()) {
            Double tf=termFreqs.get( key );
        	tf /= max;
        	termFreqs.put( key, tf );
        }
        return termFreqs;
    }

    // init tf, invertedFile, and idfs
    private void init() {
    	
        // for all docs in the database
        for (Document doc : db.values()) {
            // get the tfs for a doc
            TreeMap<String, Double> termFreqs = getTF(doc.tokenize());
            
            // add to global tf vector
            tf.add(termFreqs);

            // for all terms
            for (String term : termFreqs.keySet()) {
                // add the current docID to the posting list
                Set<Integer> docIds = invertedFile.get(term);
                if (docIds == null) docIds = new TreeSet<Integer>();
                docIds.add(doc.getId());
                invertedFile.put(term, docIds);
            }
        }

        // calculate idfs
        int dbSize = db.size();
        // for all terms
        
        
        for (Map.Entry<String, Set<Integer>> entry : invertedFile.entrySet()) {
            String term = entry.getKey();
            // get the size of the posting list, i.e. the document frequency
            
            int df = entry.getValue().size();
            //TODO write the formula for calculation of IDF    
            double idf = Math.log10((dbSize/df));
            idfs.put(term, idf);
            
            
        }
    }

	public String getDocumentsFile() {
		return documentsFile;
	}

	public void setDocumentsFile(String documentsFile) {
		this.documentsFile = documentsFile;
	}

	public String getKeywordsFile() {
		return keywordsFile;
	}

	public void setKeywordsFile(String keywordsFile) {
		this.keywordsFile = keywordsFile;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
    
    
}
