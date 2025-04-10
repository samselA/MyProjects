package pl.pw.edu.mini.zpoif.fish_watch.four_wing_api.stats.requests_converters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

/**
 * The class is used for handling GET request from Four Wings Stats API, it contains many methods for specific used cases
 */

public class FourWingsStatsGETDeclaration {
	/**
	 * URI to connect to specific vessels API
	 */
	
	private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/4wings/stats";
	
	/**
	 * map for parameters in URI
	 */
	
	private Map<String, String> parametersMap;
	
	public FourWingsStatsGETDeclaration() {
		parametersMap= new HashMap<>();
		
		parametersMap.put("datasets[0]", "public-global-fishing-effort:latest");
	}
	
	
	/**
	 * 
	 * @return returns parametersMap
	 */
	public Map<String, String> getParametersMap(){
		return parametersMap;
	}
	
	/**
	 * @param start, start date to put into body request
	 * @param end, end date to put into body request
	 */
	public void addDate(String start, String end) {
		parametersMap.put("date-range", start+","+end);
	}
	
	public void addFields(Set<FieldsValues4WingsStatsApi> fields) {
		StringBuilder builder= new StringBuilder();
		
		int counter=0;
		
		for(FieldsValues4WingsStatsApi field: fields) {
			if(counter>0) {
				builder.append(",");
			}
			String value= field.toString().replace("_", "-");
			builder.append(value);
			
			counter+=1;
		}
		parametersMap.put("fields", builder.toString());
	
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
	 * @param httpRquest, request to get HTTP response
	 * @return returns HTTP response, which might be later used to save object/objects
	 */
	
	public HttpResponse<String> sendGETRequest(HttpRequest httpRquest){
		
		HttpResponse<String> response = null;
		
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			response = httpClient.send(httpRquest, HttpResponse.BodyHandlers.ofString());
		
		}catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * @param parameters, map used in creating HTTP request
	 * @return returns HTTP, GET request
	 */
	
	
	public HttpRequest createGETRequest(Map<String, String> parameters) {
		
		HttpRequest getRequest = null;
		
		String queryString = encodeParams(parameters);
		
		try {
			URI uri = new URI(apiUrl + "?" + queryString);
			
			getRequest = HttpRequest.newBuilder()
		               .uri(uri)
		                .header("Authorization", "Bearer " + ClientInfo.getApiToken())
		                .GET()
		                .build();
			
		
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return getRequest;
		
	}
	
	/**
	 * 
	 * @param params, map which is transformed into String used in URL GET request
	 * @return returns String used in URL GET request
	 */
	
	private static String encodeParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }
	
	/**
	 * 
	 * @param value, String to be later encoded in  UTF_8 characters format
	 * @return returns encoded String in UTF_8 characters format
	 */
	
	private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
