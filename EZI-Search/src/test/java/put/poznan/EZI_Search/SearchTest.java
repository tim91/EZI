package put.poznan.EZI_Search;

import static org.junit.Assert.assertEquals;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

import put.poznan.EZI_Search.model.Document;
import put.poznan.EZI_Search.model.Query;
import put.poznan.EZI_Search.model.SearchReport;
import put.poznan.EZI_Search.reader.DocumentReader;
import put.poznan.EZI_Search.tfidf.TFIDFSol;

public class SearchTest
{

    @Test
    public void test()
    {
        String query = "artificial intelligence";
        TFIDFSol sol = TFIDFSol.getInstance();
        sol.setDocumentsFile( buildTempFilePath( "docs.txt" ) );
        sol.setKeywordsFile( buildTempFilePath( "keywords.txt" ) );
        sol.setQuery( new Query( query ) );

        SearchReport report = sol.search();
        report.printReport();
    }
    
    @Test
    public void sim()
    {
        TFIDFSol sol = TFIDFSol.getInstance();
        TreeMap<String, Double> v1=new TreeMap<String,Double>();
        TreeMap<String, Double> v2=new TreeMap<String,Double>();
        v1.put("fly", 0.1 );
        v1.put("fruit", 0.22 );
        v2.put("fly", 0.10 );
        v2.put("fruit",0.07);
        double d= sol.similarity( v1, v2 );
        System.out.println(d);
    }
    
    @Test
    public void readDocumentsFromFileTest() 
    {
        DocumentReader reader = DocumentReader.getInstance();
        reader.setDocumentFile(buildTempFilePath( "docs.txt" ));
        TreeMap<Integer,Document> db =reader.readDocumentsFromFile();
        assertEquals("david w aha  machine learning page",db.get( 0 ).getTitile());
        assertEquals(" machine learning resources suggestions welcome  wizrule zdm scientific ltd conference announcements courses on machine learning data repositories  description comprehensive machine learning resources from applications to tutorials",db.get( 0 ).getContent());
        
        System.out.println(Math.log10( 87/3 ));
    }
    
    @Test
    public void tok() 
    {
        DocumentReader reader = DocumentReader.getInstance();
        reader.setDocumentFile(buildTempFilePath( "docs.txt" ));
        TreeMap<Integer,Document> db =reader.readDocumentsFromFile();
        Document d=db.get(0);
        StringTokenizer st=d.tokenize();
        while (st.hasMoreTokens()) {
            String term = st.nextToken();

            System.out.println(term);
            
        }
            
    }

    private List<TempFile> files = new ArrayList<TempFile>();

    private String buildTempFilePath( String resourcePath )
    {
        TempFile f = new TempFile();
        files.add( f );
        InputStream is = this.getClass().getClassLoader().getResourceAsStream( resourcePath );
        try
        {
            IOUtils.copy( is, new FileOutputStream( f.getFile() ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException(e);
        }
        return f.getFile().getAbsolutePath();
    }

    @After
    public void clean()
    {
        for ( TempFile tempFile : files )
        {
            tempFile.delete();
        }
    }
}
