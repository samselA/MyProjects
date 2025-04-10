package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;
public class FourWingsPOSTReportDeclaration {
	
	/**
	 * URI to connect to specific 4Wings API
	 */
	
	private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/4wings/report";
	
	/**
	 * map for parameters in URI
	 * JSON object used in body request
	 */
	
	private Map<String, String> parametersMap;
	private JsonObject jsonBodyObject;
	
	public FourWingsPOSTReportDeclaration() {
		parametersMap= new HashMap<>();
		jsonBodyObject= new JsonObject();
		
		parametersMap.put("datasets[0]", "public-global-fishing-effort:latest");
	}
	
	/**
	 * @return returns parameters Map
	 */
	public Map<String, String> getParametersMap(){
		return parametersMap;
	}
	
	/**
	 * @return returns JSON body object
	 */
	
	public JsonObject getJsonBodyObject() {
		return jsonBodyObject;
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
	 * @param beginning, start date to filter the data
	 * @param ending, end date to filter the data
	 */
	
	public void addDateRange(String beginning, String ending) {
		parametersMap.put("date-range", beginning+','+ending);
	}
	
	/** 
	 * @param format, adds format type to given report
	 */
	
	public void addFormat(FormatValues4WingsApi format) {
		parametersMap.put("format", format.toString());
	}
	
	/** 
	 * @param group, adds group by to given report
	 */
	
	public void addGroupBy(GroupByValues4WingsApi group) {
		parametersMap.put("group-by", group.toString());
	}
	
	/** 
	 * @param resolution, adds spatial resolution to given report
	 */
	
	public void addSpatialResolution(SpatialResolution4WingsApi resolution) {
		parametersMap.put("spatial-resolution", resolution.toString());
	}
	
	/**
	 * @param resolution, adds temporal resolution to given report
	 */
	
	public void addTemporalResolution(TemporalResolution4WingsApi resolution) {
		parametersMap.put("temporal-resolution", resolution.toString());
	}
	
	
	/** 
	 * @param dataset, id of the dataset to obtain the region
	 * @param id, id of the region (example: 5690)
	 * @param bufferOperation, operation to apply to the region after the buffer
	 * @param bufferUnit, unit of the buffer value
	 * @param bufferValue, distance to draw the buffer, (negative values are allowed)
	 */
	
	public void addRegionBODY(String dataset, int id, BufferOperationsValues4WingsApi bufferOperation, BufferUnitValues4WingsApi bufferUnit, String bufferValue) {
		
		//if any String is equal "" or is null do not consider it
		//not the best solution but hope it works
		JsonObject regionObject = new JsonObject();
        
		if(dataset!="" && dataset!=null) {
			regionObject.addProperty("dataset", dataset);
		}
		if(dataset!=null) {
			 regionObject.addProperty("id", id);
		}
		if(bufferOperation!=null) {
			regionObject.addProperty("bufferOperation", bufferOperation.toString());
		}
		if(bufferUnit!=null) {
			regionObject.addProperty("bufferUnit", bufferUnit.toString());
		}
		if(bufferValue!="") {
			regionObject.addProperty("bufferValue", bufferValue);
		}
		
		if(regionObject.entrySet().isEmpty()) {
			System.out.println("Tried to add empty Region");
		}else {
			jsonBodyObject.add("region", regionObject);
		}

	}
	
	public void addGeoJsonBODY(List<List<Double>> coordinates) {
		 JsonObject geoJson = new JsonObject();
		 
		 //check if there is any different option rather than polygon
		 geoJson.addProperty("type", "Polygon");
		 
		 JsonArray coordinatesArray = new JsonArray();
		 
		 for (List<Double> coordinate : coordinates) {
	            JsonArray point = new JsonArray();
	            for (Double value : coordinate) {
	                point.add(value);
	            }
	            coordinatesArray.add(point);
		 }
	    
		 geoJson.add("coordinates", coordinatesArray);
	   
	        
	     jsonBodyObject.add("geojson", geoJson);
	     
	     
	}
	
	/** 
	 * @param parameters, map used in creating HTTP request
	 * @param jsonBodyObject, JSON object to be converted to string, used in building HTTP request
	 * @return returns POST request, which might be used in sending request to server
	 */
	
	public HttpRequest createPOSTRequest(Map<String, String> parameters, JsonObject jsonBodyObject) {
		
		HttpRequest postRequest = null;
		
		String queryString = buildQueryString(parameters);
		String requestBody = new Gson().toJson(jsonBodyObject);
		
		try {
			URI uri = new URI(apiUrl + queryString);
			
			//make sure that application/json is constant and doesn't depend from format attribute from URL
			postRequest = HttpRequest.newBuilder()
					.uri(uri)
	                .header("Authorization", "Bearer " + ClientInfo.getApiToken())
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                .build();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return postRequest;
		
	}
	
	/**
	 * @param httpRquest, request to get HTTP response
	 * @return returns HTTP response, which might be later used to save object/objects
	 */
	
	//TODO
	//think if HttpResonse<String> is correct and shouldn't be different, depending on httpRequest
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
