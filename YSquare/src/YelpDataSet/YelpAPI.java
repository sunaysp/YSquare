package YelpDataSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class YelpAPI {

	/**
	 * @param args
	 */
	
	// Set Yelp keys
	private static final String CONSUMER_KEY = "";
	private static final String CONSUMER_SECRET = "";
	private static final String TOKEN = "";
	private static final String TOKEN_SECRET = "";
	
	private static final String SEARCH_PATH = "/v2/search";
	private static final String BUSINESS_PATH = "/v2/business";

  OAuthService service;
  Token accessToken;
	
  	public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
	    this.service =
	        new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
	            .apiSecret(consumerSecret).build();
	    this.accessToken = new Token(token, tokenSecret);
	}
	
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com" + path);
        return request;
      }
    
  	public String GetBusinessByCategory(String category, String location) {
  		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
  		request.addQuerystringParameter("term", category);
  		request.addQuerystringParameter("location", location);
  		request.addQuerystringParameter("offset", "20");
  		request.addQuerystringParameter("limit", "20");
  		return sendRequestAndGetResponse(request); 
  	}
  	
    private String sendRequestAndGetResponse(OAuthRequest request) {
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
      }


	public static void main(String[] args) {
		
		YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
		try {
			BufferedReader br = new BufferedReader(new FileReader("data\\part-r-00000-coffee"));
			String line = null;
			while((line=br.readLine())!=null) {
				String[] data = line.split("-",	2);
				String zipCode = data[0];
				String[] categories = data[1].split("\\s+");
				StringBuilder category = new StringBuilder();
				
				for(int i=0;i< categories.length-1;i++ ) {
					category.append(categories[i]);				
				}
				
				// Get data from Yelp
				String responseJSON = yelpApi.GetBusinessByCategory(category.toString(), zipCode);
				JSONParser p = new JSONParser();
				JSONObject jsondata = (JSONObject)p.parse(responseJSON);
				JSONArray businesses = (JSONArray) jsondata.get("businesses");
			  
			    for(Object obj: businesses) {
			    	JSONObject business = (JSONObject) obj;
			    	System.out.println(zipCode + "\t" + category+ "\t" + business.get("rating").toString()+ "\t" +categories[categories.length-1]);
			    	
			    	try{
			    		 BufferedWriter output = new BufferedWriter(new FileWriter(new File("data/YelpRatings-coffee.txt"),true));
			             output.write(zipCode + "\t" + category+ "\t" +business.get("rating").toString() + "\t" + categories[categories.length-1] + "\n");
			             output.close();
					}catch (IOException e) {
						e.printStackTrace();
					}	
			    }
			}
		} catch (ParseException e){ 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}

// References http://www.yelp.com/developers/documentation/v2/search_api
// http://www.yelp.com/developers/documentation/v2/authentication
