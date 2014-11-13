package pl.tstraszewski.zad2;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearchApp {
	
	public LuceneSearchApp() {

	}
	
	private static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
	
	private static String indexPath;
	
	public void index(List<RssFeedDocument> docs) {

		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new StandardAnalyzer());
		config.setOpenMode(OpenMode.CREATE);
		try {
			IndexWriter iw = new IndexWriter(new SimpleFSDirectory(new File(indexPath)), config);
			
			for (RssFeedDocument rssFeedDocument : docs) {
				Document d = createdDoc(rssFeedDocument);
				iw.addDocument(d);
			}
			iw.commit();
			iw.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private Document createdDoc(RssFeedDocument rssDoc){
		
		Document d = new Document();
		
		TextField titleField = new TextField("title", rssDoc.getTitle(), Store.YES);
		TextField descField = new TextField("description", rssDoc.getDescription(), Store.NO);
		LongField dateField = new LongField("date", rssDoc.getPubDate().getTime(),Store.NO);
		
		//add field do doc
		d.add(titleField);
		d.add(descField);
		d.add(dateField);
		//document ready, return
		return d;
		
	}
	
	public List<String> search(List<String> inTitle, List<String> notInTitle, List<String> inDescription, List<String> notInDescription, String startDate, String endDate) {
		
		printQuery(inTitle, notInTitle, inDescription, notInDescription, startDate, endDate);

		List<String> results = new LinkedList<String>();

		BooleanQuery mainQuery = new BooleanQuery();
		
		addBooleanCaluse("title",inTitle,Occur.MUST,mainQuery);
		addBooleanCaluse("title",notInTitle,Occur.MUST_NOT,mainQuery);
		
		addBooleanCaluse("description", inDescription, Occur.MUST, mainQuery);
		addBooleanCaluse("description", notInDescription, Occur.MUST_NOT, mainQuery);
		
		
		long lowerTerm;
		long upperTerm;
	
		
		if(startDate != null || endDate != null){
			if(startDate != null){
				Date sd = null;
				try {
					sd = dateParser.parse(startDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lowerTerm = sd.getTime();
			}else{
				lowerTerm = Long.MIN_VALUE;
			}
			
			if(endDate != null){
				Date ud = null;
				try {
					ud = dateParser.parse(endDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				upperTerm = ud.getTime();
				//add 24h, time must point to the end of the day
				upperTerm += 24 * 60 * 60 * 1000 - 1;
			}else{
				upperTerm = Long.MAX_VALUE;
			}
			
			NumericRangeQuery<Long> numericRange = NumericRangeQuery.newLongRange("date", lowerTerm, upperTerm, true, true);
			
			mainQuery.add(new BooleanClause(numericRange, Occur.MUST));
		}
		
		

		
//		System.out.println("Generated query: " + mainQuery.toString());
		
		// implement the Lucene search here

		try {
			DirectoryReader dirReader = DirectoryReader.open(new SimpleFSDirectory(new File(indexPath)));
			
			IndexSearcher is = new IndexSearcher(dirReader);
			TopDocs topDocs = is.search(mainQuery, Integer.MAX_VALUE);
			
			for(ScoreDoc sd : topDocs.scoreDocs){
				Document foundedDoc = is.doc(sd.doc);
				IndexableField titleField = foundedDoc.getField("title");
				results.add(titleField.stringValue());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return results;
	}
	
	private void addBooleanCaluse(String fieldName, List<String> terms, Occur occur,BooleanQuery mainQuery){
		
		if(terms != null && terms.size() > 0){
			BooleanQuery bq = new BooleanQuery();
			for (String term : terms) {
				TermQuery tq = new TermQuery(new Term(fieldName,term));
				mainQuery.add(new BooleanClause(tq, occur));
			}
		}
		
	}
	
	public void printQuery(List<String> inTitle, List<String> notInTitle, List<String> inDescription, List<String> notInDescription, String startDate, String endDate) {
		System.out.print("Search (");
		if (inTitle != null) {
			System.out.print("in title: "+inTitle);
			if (notInTitle != null || inDescription != null || notInDescription != null || startDate != null || endDate != null)
				System.out.print("; ");
		}
		if (notInTitle != null) {
			System.out.print("not in title: "+notInTitle);
			if (inDescription != null || notInDescription != null || startDate != null || endDate != null)
				System.out.print("; ");
		}
		if (inDescription != null) {
			System.out.print("in description: "+inDescription);
			if (notInDescription != null || startDate != null || endDate != null)
				System.out.print("; ");
		}
		if (notInDescription != null) {
			System.out.print("not in description: "+notInDescription);
			if (startDate != null || endDate != null)
				System.out.print("; ");
		}
		if (startDate != null) {
			System.out.print("startDate: "+startDate);
			if (endDate != null)
				System.out.print("; ");
		}
		if (endDate != null)
			System.out.print("endDate: "+endDate);
		System.out.println("):");
	}
	
	public void printResults(List<String> results) {
		if (results.size() > 0) {
			Collections.sort(results);
			for (int i=0; i<results.size(); i++)
				System.out.println(" " + (i+1) + ". " + results.get(i));
		}
		else
			System.out.println(" no results");
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			LuceneSearchApp engine = new LuceneSearchApp();
			
			RssFeedParser parser = new RssFeedParser();
			parser.parse(args[0]);
			List<RssFeedDocument> docs = parser.getDocuments();
			
			indexPath = args[1];
			engine.index(docs);

			List<String> inTitle;
			List<String> notInTitle;
			List<String> inDescription;
			List<String> notInDescription;
			List<String> results;
			
			// 1) search documents with words "kim" and "korea" in the title
			inTitle = new LinkedList<String>();
			inTitle.add("kim");
			inTitle.add("korea");
			results = engine.search(inTitle, null, null, null, null, null);
			engine.printResults(results);
			
			// 2) search documents with word "kim" in the title and no word "korea" in the description
			inTitle = new LinkedList<String>();
			notInDescription = new LinkedList<String>();
			inTitle.add("kim");
			notInDescription.add("korea");
			results = engine.search(inTitle, null, null, notInDescription, null, null);
			engine.printResults(results);

			// 3) search documents with word "us" in the title, no word "dawn" in the title and word "" and "" in the description
			inTitle = new LinkedList<String>();
			inTitle.add("us");
			notInTitle = new LinkedList<String>();
			notInTitle.add("dawn");
			inDescription = new LinkedList<String>();
			inDescription.add("american");
			inDescription.add("confession");
			results = engine.search(inTitle, notInTitle, inDescription, null, null, null);
			engine.printResults(results);
			
			// 4) search documents whose publication date is 2011-12-18
			results = engine.search(null, null, null, null, "2011-12-18", "2011-12-18");
			engine.printResults(results);
			
			// 5) search documents with word "video" in the title whose publication date is 2000-01-01 or later
			inTitle = new LinkedList<String>();
			inTitle.add("video");
			results = engine.search(inTitle, null, null, null, "2000-01-01", null);
			engine.printResults(results);
			
			// 6) search documents with no word "canada" or "iraq" or "israel" in the description whose publication date is 2011-12-18 or earlier
			notInDescription = new LinkedList<String>();
			notInDescription.add("canada");
			notInDescription.add("iraq");
			notInDescription.add("israel");
			results = engine.search(null, null, null, notInDescription, null, "2011-12-18");
			engine.printResults(results);
		}
		else
			System.out.println("ERROR: the path of a RSS Feed file has to be passed as a command line argument.");
	}
}
