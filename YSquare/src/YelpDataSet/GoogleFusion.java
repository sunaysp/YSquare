package YelpDataSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class GoogleFusion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader("data\\TopRecommendations.txt"));
			BufferedWriter output = new BufferedWriter(new FileWriter(new File("data/GoogleFusionInput-Coffee.txt"),true));
			String line = null;
			LinkedHashMap<Integer, String> h = new LinkedHashMap<Integer, String>();
			while((line=br.readLine())!=null) {
				String[] data = line.split("\t",2);
				int Key = Integer.parseInt(data[0]);
				System.out.println("key will be  "+data[0]);
				System.out.println("data "+data[1]);
				if(!h.containsKey(Key)) {
					h.put(Key, data[1]);
				}
				else
				{
					String val = h.get(Key);
					val = data[1] + "\t"+ val;
					h.put(Key,val);
				}
			}
			
			Iterator<Integer> i = h.keySet().iterator();
			while(i.hasNext()) {
				int key = i.next();
				output.write(key+"\t"+h.get(key)+"\n");
				System.out.println(key+"\t"+h.get(key));
			}
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
