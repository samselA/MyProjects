package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

/**
 * The class is used for handling POST request from 4Wings API, it contains many methods for specific used cases
 */

public class FourWingsPOSTMapDeclaration {
	
	/**
	 * URI to connect to specific 4Wings API
	 */
	
	private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/4wings/generate-png";
	
	/**
	 * map for parameters in URI
	 */
	
	private Map<String, String> parametersMap;
	
	public FourWingsPOSTMapDeclaration() {
		parametersMap= new HashMap<>();
		
		parametersMap.put("datasets[0]", "public-global-fishing-effort:latest");
	}
	
	public Map<String, String> getParametersMap(){
		return parametersMap;
	}
	
	/**
	 * @param color, Color in hexadecimal used to generate the color ramp
	 */
	
	public void addColor(String color) {
		parametersMap.put("color", color);
	}
	
	/**
	 * @param interval, time resolution added to tiles 
	 */
	
	public void addInterval(IntervalValues4WingsApi interval) {
		String value= interval.toString();
		if(value=="_10DAYS") {
			value= "10DAYS";
		}
		parametersMap.put("interval", value);
	}
	
	/**
	 * @param beginning, start date to filter the data
	 * @param ending, end date to filter the data
	 */
	
	public void addDateRange(String beginning, String ending) {
		parametersMap.put("date-range", beginning+','+ending);
	}
	
	/**
	 * @param things, flags/gearTypes to be considered in data
	 * @param flags, checks what type of data is considered
	 */
	
	public void addFlagsORGeartypesFilter(String[] things, boolean flags) {
		StringBuilder stringBuilder = new StringBuilder("(");
		
		for (int i = 0; i < things.length; i++) {
            stringBuilder.append("\"").append(things[i]).append("\"");

            if (i < things.length - 1) {
                stringBuilder.append(", ");
            }
        }
		stringBuilder.append(")");
		
		String solution= stringBuilder.toString();
		
		if (flags) {
			parametersMap.put("filters[0]", "flags "+solution);
		}else {
			parametersMap.put("filters[0]", "geartype "+solution);
		}
	}
	
	/**
	 * @param parameters, parameters used in creating POST request
	 * @return returns HTTP request, used in sending POST request
	 */
	
	public HttpRequest createPOSTRequest(Map<String, String> parameters) {

		HttpRequest postRequest = null;

		String queryString = buildQueryString(parameters);

		try {
			URI uri = new URI(apiUrl + queryString);

			postRequest = HttpRequest.newBuilder()
					.uri(uri)
	                .header("Authorization", "Bearer " + ClientInfo.getApiToken())
	                .header("Content-Type", "application/x-www-form-urlencoded")
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return postRequest;
	}
	
	/**
	 * @param httpRequest, request to be send to server
	 * @return returns httpResponse, which is server response
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
	
	/**
	 * @param data, map used for building query
	 * @return returns URL, used for POST request
	 */
	
	private static String buildQueryString(Map<String, String> data) {
        var builder = new StringBuilder("?");
        for (var entry : data.entrySet()) {
            if (builder.length() > 1) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), java.nio.charset.StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), java.nio.charset.StandardCharsets.UTF_8));
        }
        return builder.toString();
    }
}
