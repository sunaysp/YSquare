package ExtractFourSquare;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;


 
public class FSReducer 
extends Reducer<Text, IntWritable, Text, IntWritable> {
	 @Override
	  public void reduce(Text key, Iterable<IntWritable> values,
	      Context context)
	      throws IOException, InterruptedException {

		 
		 int sum =0;
		 System.out.println("Reducer "+key);
		 
			 Iterator<IntWritable> it = values.iterator();

			 while(it.hasNext()) {
				 sum=sum+it.next().get();
			 }
			 System.out.println("Done for Key "+key+ "  Sum: "+sum);
		 context.write(key, new IntWritable(sum));
	 }

}



// coffeeshop 10026 25


// coffeeshop 10026 4.5


// Key: coffeeshop Values(List): 10026-1, 10026-1, 10041-1,.....
// Map<k,v> 10026, 0 
//			10041,0

// Iterate values
// Split with -, get zipcode, count
// Update Map<k,v> Increment v with count


// Context: coffeeshop, 10026-2
//			coffeeshop, 10041-5




