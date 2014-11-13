package pl.tstraszewski;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateTest {

	private static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void test(){
		
		String d = "2011-12-18";
		
		try {
			
			Date start = dateParser.parse(d);
			
			System.out.println(start);
			
			Long endL = start.getTime();
			endL += 24 * 60 * 60 * 1000 - 1;
			Date end = new Date(endL);
			
			System.out.println(end);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
