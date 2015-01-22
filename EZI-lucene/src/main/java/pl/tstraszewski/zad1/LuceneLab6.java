package pl.tstraszewski.zad1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class LuceneLab6
{
    // TODO follows TODOs; there four places that should be filled with code
    // according to the instructions given in Lab6.pdf

    // directory where the index would be placed in
    // provided by the user as args[1]; set in main()
    public static String indexPath;

    // TODO create the index, fill it with documents (use indexDoc function), close the index
    public static void createIndex( String path )
        throws Exception
    {

        System.out.println( "I'll will create index, feed it, and close it - still to be implemented" );

        IndexWriter iw = new IndexWriter( indexPath, new StandardAnalyzer(), true );
        File library = new File( path );

        if ( library.exists() && library.isDirectory() )
        {
            File[] docs = library.listFiles();

            for ( File doc : docs )
            {
                Document d = indexDoc( doc );
                iw.addDocument( d );
            }
            // optimize index
            iw.optimize();
            iw.flush();
            iw.close();

        }

    }

    // TODO create object of class Document; create some necessary fields (e.g. path, content)
    // call this function in createIndex() to create Documents that would be subsequently added to the index
    public static Document indexDoc( File docPath )
        throws Exception
    {

        FileInputStream fis = new FileInputStream( docPath );

        Document d = new Document();
        d.add( new Field( "path", docPath.getAbsolutePath(), Store.YES, Field.Index.TOKENIZED ) );
        d.add( new Field( "content", (Reader) new InputStreamReader( fis ) ) );

        return d;
    }

    // TODO create objects of class: Analyzer, IndexSearcher, QueryParser, Query and Hits
    // for Analyzer remember to use the same constructor as for IndexWriter
    // for QueryParser indicate the fields to be analyzed
    // for Query you should parse "queryString" which is given as a parameter of the function
    // for Hits you should search results for a given query and return them
    public static Hits processQuery( String queryString, IndexSearcher searcher )
        throws IOException, ParseException
    {

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser( "content", analyzer );
        Query q = null;
        try
        {
            q = parser.parse( queryString );
        }
        catch ( org.apache.lucene.queryParser.ParseException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Hits hits = searcher.search( q );
        return hits;
    }

    public static void main( String[] args )
    {
        if ( args.length < 2 )
        {
            System.out.println( "java -cp lucene-core-2.2.0.jar:. BasicIRsystem texts_path index_path" );
            System.out.println( "need two args with paths to the collection of texts and to the directory where the index would be stored, respectively" );
            System.exit( 1 );
        }
        try
        {
            String textsPath = args[0];
            indexPath = args[1];
            createIndex( textsPath );
            IndexSearcher is = new IndexSearcher( indexPath );
            String query = " ";

            // process queries until one writes "lab6"
            while ( true )
            {
                Scanner sc = new Scanner( System.in );
                System.out.println( "Please enter your query: (lab9 to quit)" );
                query = sc.next();

                if ( query.equals( "lab9" ) )
                {
                    break;
                } // to quit

                Hits hits = processQuery( query, is );

                if ( hits != null )
                {
                    System.out.println( hits.length() + " result(s) found" );

                    Iterator iter = hits.iterator();
                    while ( iter.hasNext() )
                    {
                        Hit hit = (Hit) iter.next();

                        try
                        {

                            Document foundedDoc = hit.getDocument();
                            Field pathFiled = foundedDoc.getField( "path" );
                            System.out.println( pathFiled.stringValue() + " : " + hit.getScore() );

                        }
                        catch ( Exception e )
                        {
                            System.err.println( "Unexpected exception" );
                            System.err.println( e.toString() );
                        }
                    }
                }
                else
                {
                    System.out.println( "Processing the query still not implemented, heh?" );
                }
            }

        }
        catch ( Exception e )
        {
            System.err.println( "Even more unexpected exception" );
            System.err.println( e.toString() );
        }
    }
}