package pl.tstraszewski.ezi.hadoop.cw;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Mapper.Context;

/**
 * Class represents Mapper, Combiner and Reducer
 */
public class DateMapperCombinerAndReducer extends MapReduceBase implements Mapper<Object, Text, Text, IntWritable>,Reducer<Text,IntWritable,Text,IntWritable>
{

	private Text date = new Text();
	private IntWritable ONE = new IntWritable(1);

	
	@Override
	/**
	 * Method implements Mapper logic
	 * Key - String with represents date in format YYYY-MM-DD
	 * Value - Integer with value 1
	 */
	public void map(Object arg0, Text values,
			OutputCollector<Text, IntWritable> out, Reporter arg3)
			throws IOException {
		
		String line = ((Text)values).toString();
		
	
		if(line.matches("^\\d+:$")) {
			/* This is the Movie ID line. Ignore it */
		} else {
			String[] els = line.split(",");
			date.set(els[2]);
			
			/* Add them to the output */
			out.collect(date, ONE);
			
		}
		
	}
	
	/**
	 * Combiner, Reducer (key: date as string in format YYYY-mm-dd, value: array of ones to sum)
	 * Combiner - sum creates locally and reduces the number of transmitted data
	 * Reducer - creates a total sum
	 */
	@Override
	public void reduce(Text date, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> out, Reporter arg3)
			throws IOException {
		
		int sum = 0;
		
		while(values.hasNext()){
			sum += values.next().get();
		}
	
		out.collect(date, new IntWritable(sum));
		
	}
}
