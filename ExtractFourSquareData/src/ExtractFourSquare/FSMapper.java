package ExtractFourSquare;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class FSMapper
extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	public void map(LongWritable key, Text value, Context context)
	   throws IOException, InterruptedException {
		String line = value.toString();
		//int[] NYzipCodes = {};
		
		if(!line.isEmpty()) {
			String[] splitValues = line.split("\\s+", 2);
			for(String s : splitValues) {
			System.out.print(s+ " ");
			}
			String zipCode = splitValues[0].replaceAll("\\s+$", "");
			String category = splitValues[1].replaceAll("\\s+$", "");
			System.out.println(zipCode + " -- " + category);

			context.write(new Text(zipCode+"-"+category), new IntWritable(1));
		}
	}
}

// arr[0]- 10026
// 
