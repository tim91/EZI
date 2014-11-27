package pl.tstraszewski.ezi.hadoop.cw;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class Driver {

	public static void main(String[] args) {
		/* Require args to contain the paths */
		if(args.length != 1 && args.length != 2) {
			System.err.println("Error! Usage: \n" +
					"HadoopDriver <input dir> <output dir>\n" +
					"HadoopDriver <job.xml>");
			System.exit(1);
		}
		
		JobClient client = new JobClient();
		JobConf conf = null;
		
		if(args.length == 2) {
			conf = new JobConf(Driver.class);
			
			conf.setMapOutputKeyClass(Text.class);
			conf.setMapOutputValueClass(IntWritable.class);
			
			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.setInputPaths(conf, new Path(args[0]));
		    FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	
			conf.setMapperClass(DateMapperCombinerAndReducer.class);
			conf.setCombinerClass(DateMapperCombinerAndReducer.class);
			conf.setReducerClass(DateMapperCombinerAndReducer.class);
			
			conf.set("mapred.child.java.opts", "-Xmx1024m");
		} else {
			conf = new JobConf(args[0]);
		}
		
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
