package pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.requests_converters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

/**
 * The class is used for handling POST request from vessels Insights API, it contains many methods for specific used cases
 */

public class VesselsInsightsPOSTDeclaration {
	
	/**
	 * URI to connect to specific vessels API
	 */
	
	private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/insights/vessels";
	
	
	/**
	 * used for building body request part of POST method
	 */
	
	private JsonObject jsonBodyObject;
	
	public VesselsInsightsPOSTDeclaration() {
		jsonBodyObject= new JsonObject();
	}
	
	/**
	 * @return returns JSON body object
	 */
	
	public JsonObject getJsonBodyObject() {
		return jsonBodyObject;
	}
	
	/**
	 * @param start, start date to put into body request
	 * @param end, end date to put into body request
	 */
	public void addDate(String start, String end) {
		jsonBodyObject.addProperty("startDate", start);
		jsonBodyObject.addProperty("endDate", end);
	}
	
	/**
	 * @param included, ENUM to convert to string to put into body request
	 */
	
	public void addIncludes(IncludesValuesInsightsAPI included) {
		JsonArray includesArray = new JsonArray();
		includesArray.add(included.toString());
		jsonBodyObject.add("includes", includesArray);
	}
	
	/**
	 * @param vesselID, vessel to put into body request
	 */
	public void addVessels(String vesselID) {
		JsonArray vesselsArray = new JsonArray();
		JsonObject vesselObject = new JsonObject();
		
		vesselObject.addProperty("datasetId", "public-global-vessel-identity:latest");
		vesselObject.addProperty("vesselId", vesselID);
		vesselsArray.add(vesselObject);
		
		jsonBodyObject.add("vessels", vesselsArray);
	}
	
	/**
	 * @param jsonBodyObject, JSON object to be converted to string, used in building HTTP request
	 * @return returns POST request, which might be used in sending request to server
	 * @throws URISyntaxException, whenever URI doesn't exist throws exception
	 */
	
	public HttpRequest createPOSTRequest(JsonObject jsonBodyObject) throws URISyntaxException {
		String requestBody = new Gson().toJson(jsonBodyObject);
		
		HttpRequest postRequest= null;
		
		URI uri = new URI(apiUrl);
		
		postRequest= HttpRequest.newBuilder()
		            .uri(uri)
		            .header("Authorization", "Bearer " + ClientInfo.getApiToken())
		            .header("Content-Type", "application/json")
		            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
		            .build();

		return postRequest;
	}
	
	/**
	 * @param httpRquest, request to get HTTP response
	 * @return returns HTTP response, which might be later used to save object/objects
	 */
	
	public HttpResponse<String> sendPOSTRequest(HttpRequest httpRequest){
		
		HttpResponse<String> response= null;
		
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		
		}catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
	
}
