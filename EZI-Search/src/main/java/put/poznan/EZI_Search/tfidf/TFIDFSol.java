package put.poznan.EZI_Search.tfidf;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import put.poznan.EZI_Search.model.Document;
import put.poznan.EZI_Search.model.Query;
import put.poznan.EZI_Search.reader.DocumentReader;

public class TFIDFSol {
	
	TreeMap<Integer,Document> db = new TreeMap<Integer,Document>(); // the document collection
    TreeMap<String, Double> idfs = new TreeMap<String, Double>(); // idf value for each term in the vocabulary
    TreeMap<String, Set<Integer>> invertedFile = new TreeMap<String, Set<Integer>>(); // term -> docIds of docs containing the term
    Vector<TreeMap<String, Double>> tf = new Vector<TreeMap<String, Double>>(); // term x docId matrix with term frequencies

    public static void main(String [] args) {
    	TFIDFSol tfidf = new TFIDFSol();
    	tfidf.go();
    }

    private void go() {
        // init the database
        initDB("./documents");

        // init global variables: tf, invertedFile, and idfs
        init();

        // print the database
        printDB();

        // idfs and tfs
        System.out.println("IDFs:");
        // print the vocabulary
        printVoc();
        
        System.out.println("\nTFs for Equations:");
        for (int i = 0; i < db.size(); i++)
        {
        	    System.out.println("Equations: doc " + i + " : " + getTF("Equations", i));
        }
    
        // similarities for different queries
        rank(new Query("Differential Equations"));
    }

    // inits database from textfile
    private void initDB(String directory) {
        db.clear();
        DocumentReader reader = DocumentReader.getInstance();
        reader.setDocumentDirectory(directory);
        db = reader.readDocuments();
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
    private double similarity(TreeMap<String, Double> v1, TreeMap<String, Double> v2) {
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
    private void rank(Query query) {
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

        // helper class to store a docId and its score
        class DocScore implements Comparable<DocScore> {
            double score;
            int docId;

            public DocScore(double score, int docId) {
                this.score = score;
                this.docId = docId;
            }

            public int compareTo(DocScore docScore) {
                if (score > docScore.score) return -1;
                if (score < docScore.score) return 1;
                return 0;
            }
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
        for (DocScore docScore : scores) {
            System.out.println(db.get(docScore.docId).getTitile() + "; " + docScore.score);
        }
    }

    // returns the idf of a term
    private double idf(String term) {
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
            count++;
            termFreqs.put(term, count);
            if (count > max) max = count;
        }

        // normalize tf
        for (Double tf : termFreqs.values()) {
        	//TODO write the formula for normalization of TF
        	tf /= max;
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
        
        //Add keywords
//        Vector<String> keywords = KeywordsReader.getInstance().getKeywords();
//        
//        for (String term : keywords) {
//            // add the current docID to the posting list
//            Set<Long> docIds = invertedFile.get(term);
//            if (docIds == null) docIds = new TreeSet<Long>();
//            
//            //jezeli term nie wystepowal, to docIds bedzie puste.... przy idf bedzie dzielenie przez 0
//            
//            invertedFile.put(term, docIds);
//        }
        
        
        for (Map.Entry<String, Set<Integer>> entry : invertedFile.entrySet()) {
            String term = entry.getKey();
            // get the size of the posting list, i.e. the document frequency
            
            int df = entry.getValue().size();
            //TODO write the formula for calculation of IDF    
            double idf = Math.log(dbSize/df);
            idfs.put(term, idf);
            
            
        }
    }
}
