package Foursquare;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

import javax.json.JsonException;

public class extractData {
	
	//Set Four Square clientID and ClientSecret ID
	private String clientID = "";
	private String clientSecret = "";
	private String version = "20141101";
	private String zipCode = "10001";
	private String category = "sushi";	
	
	String [] zipCodeManhattan = {"10026", "10027", "10030", "10037", "10039", "10001", "10018",
			 "10019", "10020", "10036", "10029", "10035", "10010", "10016", "10017", "10022",
			 "10012", "10013", "10014", "10004", "10005", "10006", "10007", "10038", "10280",
			 "10002", "10003", "10009", "10021", "10028", "10044", "10128", "10023", "10024",
			 "10025", "10031", "10032", "10033", "10034", "10040"};
	
	String [] zipCodeManhattan2 = {"10001", "10005", "10018",
			 "10002", "10003", "10009", "10021", "10028", "10044", "10128", "10023", "10024",
			 "10025", "10031", "10032", "10033", "10034", "10040"};
	
	public static void main (String [] args)
	{
		extractData foursq = new extractData();
		foursq.createJSONObject();
	
	}
	
	/*Get user ID's who like the venue
	 * https://api.foursquare.com/v2/venues/4d1266ff12916dcb8daad58a/likes?oauth_token=CFKRBCTL24DCHGC0DZSLCVKF1JM5IPBMG1SBSMZCA201UPUM&v=20131011
	 * 
	 */
	
	/*
	 * Get other likes of a specific user ID
	 * https://api.foursquare.com/v2/users/18221715/venuelikes?oauth_token=CFKRBCTL24DCHGC0DZSLCVKF1JM5IPBMG1SBSMZCA201UPUM&v=20141112
	 */
	
	private void createJSONObject()
	{
		Foursquare fsq = new Foursquare();
		ArrayList<Foursquare> listOfVenue = new ArrayList<Foursquare>(); 
		
		try {  
			 JSONParser jsonParser = new JSONParser();
			 JSONObject json;

			
			//try {
			/*	BufferedReader br = null;
				String sCurrentLine;
				 
				br = new BufferedReader(new FileReader("/Users/ddevika/Documents/NYUPolytech/Fall2014/Big Data Analytics/Project/DataDump/10026_23.json"));
	 
				while ((sCurrentLine = br.readLine()) != null) {
				//	System.out.println(sCurrentLine);
				}
				br.close();*/
				
				//JSONObject jsonString = (JSONObject) jsonParser.parse(new FileReader("/Users/ddevika/Documents/NYUPolytech/Fall2014/Big Data Analytics/Project/DataDump/1117_11PM/10001_2014-11-17_23.json"));
			 	for (int zip=0; zip< zipCodeManhattan.length; zip++)
			 	{
			 	String jsonString = callURL("https://api.foursquare.com/v2/venues/search?&near="+zipCodeManhattan2[zip]+"&query="+category+"&client_id="+clientID+"&client_secret="+clientSecret+"&v="+version+"");		
				
				json = (JSONObject) jsonParser.parse(jsonString);
				JSONObject categories = (JSONObject) json.get("response");
				JSONArray location= (JSONArray) categories.get("venues");
				ArrayList<String> venueId = new ArrayList<String>();

				
				for (int i=0; i<location.size(); i++)
				{
					JSONObject temp = (JSONObject) location.get(i);

					fsq.setCategory(category);
					fsq.setVenueName(temp.get("name").toString());
					fsq.setCheckInCount(temp.get("stats").toString());
					fsq.setVenueId(temp.get("id").toString());
					venueId.add(temp.get("id").toString());
					listOfVenue.add(fsq);
				}
	
				//get user id's
				getUserId(venueId, zipCodeManhattan[zip]);
				
			} 
		}
			catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			/*catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
	//	} 
	     /*catch (JsonException e) {
			e.printStackTrace();
		} */
	}
	
	private void getUserId(ArrayList<String> venueId, String zipCode)
	{
		//HashMap<String, ArrayList<String>> venueUserMap = new HashMap<String, ArrayList<String>>();

		for (int i=0; i<venueId.size(); i++)
		{
			ArrayList<String> userId = new ArrayList<String>();

			// Set Oauth_token value
			String jsonString = callURL("https://api.foursquare.com/v2/venues/"+venueId.get(i)+"/likes?oauth_token=&v=20131011");		
			
			JSONParser jsonParser = new JSONParser();
			
			JSONObject json;
						
			try {
				json = (JSONObject) jsonParser.parse(jsonString);
				JSONObject categories = (JSONObject) json.get("response");
				JSONObject likes = (JSONObject) categories.get("likes");
				JSONArray groups= (JSONArray) likes.get("groups");
				
				if (categories.isEmpty())
				{
					continue;
				}
				
				if (groups.isEmpty())
				{
					continue;
				}
				
				else
				{
					JSONObject items = (JSONObject) groups.get(0);
					JSONArray itemsArray = (JSONArray) items.get("items");

																	
					for (int j=0; j<itemsArray.size(); j++)
					{
						JSONObject temp = (JSONObject) itemsArray.get(j);
						
						userId.add(temp.get("id").toString());
					}
					getUserFavorites(userId, zipCode);
				
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			//venueUserMap.put(venueId.get(i), userId);
			//userId.clear();
			
		}//for loop

		//System.out.println(venueUserMap);
		
	}
	
	private void getUserFavorites(ArrayList<String> userId, String zipCode)
	{
		ArrayList<String> userFavorites = new ArrayList<String>();

		
		for (int i=0; i<userId.size(); i++)
		{

			if (userId.get(i).equals("23507715") || userId.get(i).equals("500397") || userId.get(i).equals("887703") || userId.get(i).equals("16309328") || userId.get(i).equals("18899831"))
			{
				continue;
			}
			
			try
			{
			// Set Oauth_token value
			String jsonString = callURL("https://api.foursquare.com/v2/users/"+userId.get(i)+"/venuelikes?oauth_token=&v=20141112");		
			
			JSONParser jsonParser = new JSONParser();
			
			JSONObject json;
		
				json = (JSONObject) jsonParser.parse(jsonString);
				JSONObject response = (JSONObject) json.get("response");
				JSONObject venues = (JSONObject) response.get("venues");
				JSONArray items = (JSONArray) venues.get("items");
								
				for (int j=0; j<items.size(); j++)
				{
					JSONObject temp = (JSONObject) items.get(j);
					JSONArray temp2 = (JSONArray) temp.get("categories");
										
					for (int k=0; k<temp2.size(); k++)
					{
						JSONObject categories = (JSONObject) temp2.get(k);
						userFavorites.add(categories.get("shortName").toString());
					}
					
				}
				

			}
			catch(Exception e)
			{
				System.out.println("exception caught " + e.getMessage());
				continue;
			}
			
		}
		writeToFile(userFavorites, zipCode);

		
	}
	
	private void writeToFile(ArrayList<String> userFavorites, String zipCode)
	{
		BufferedWriter bufferWriter = null;
		try {
			// Give File location
			File file =new File("/Users/.../NYUPolytech/Fall2014/Big Data Analytics/Project/mapperInput3.txt");
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
	        bufferWriter = new BufferedWriter(fileWriter);

    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    			//true = append file
    	        for (String str: userFavorites)
    	        {
    	        	bufferWriter.write(zipCode + " "+ str);
    	        	bufferWriter.write("\n");
    	        }
    	        
    	        bufferWriter.flush();
    	        bufferWriter.close();
    	       
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String callURL(String myURL) {
		// TODO Auto-generated method stub
		//System.out.println("Requested URL: " + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	
}
