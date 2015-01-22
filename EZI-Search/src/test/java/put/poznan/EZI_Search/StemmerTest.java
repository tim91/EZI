package put.poznan.EZI_Search;

import org.junit.Test;

import put.poznan.EZI_Search.Stemmer.Stemmer;

public class StemmerTest
{

    @Test
    public void test()
    {
        Stemmer s = new Stemmer();
        String x = "Ltd. Conference Announcements. Courses on Machine Learning. Data Repositories";
        s.add( x.toCharArray(), x.length() );
        s.stem();
        System.out.println( s.toString() );

        s.add( x.toCharArray(), x.length() );
        s.stem();
        System.out.println( s.toString() );
    }

}
