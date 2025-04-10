package pl.pw.edu.mini.zpoif.fish_watch.demonstrator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import pl.pw.edu.mini.zpoif.fish_watch.events_api.EventsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.events_api.EventsGETDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.events_api.Root;
import pl.pw.edu.mini.zpoif.fish_watch.four_wing_api.stats.requests_converters.FieldsValues4WingsStatsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wing_api.stats.requests_converters.FourWingsStatsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wing_api.stats.requests_converters.FourWingsStatsGETDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.Entry;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.StyleConfiguration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FormatValues4WingsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTMapDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTMapDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTReportDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTReportDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.GroupByValues4WingsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.IntervalValues4WingsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.SpatialResolution4WingsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.TemporalResolution4WingsApi;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.stats.json_classes.WorldwideStats;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes.Response;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters.VesselsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters.VesselsGETDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.json_classes.InsightsPeriodGap;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.requests_converters.IncludesValuesInsightsAPI;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.requests_converters.VesselsInsightsPOSTDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.requests_converters.VesselsInsightsPOSTDeclarationToObjects;


/**
 * @author Igor Rudolf, Aleksandra Samsel
 * @version v.0.01
 */

public class Demonstrator {

	public static void main(String[] args) throws Exception {

		String apiToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImtpZEtleSJ9.eyJkYXRhIjp7Im5hbWUiOiJSRVNUX2Zpc2hfd2F0Y2giLCJ1c2VySWQiOjMwMDY5LCJhcHBsaWNhdGlvbk5hbWUiOiJSRVNUX2Zpc2hfd2F0Y2giLCJpZCI6MTEzMywidHlwZSI6InVzZXItYXBwbGljYXRpb24ifSwiaWF0IjoxNzAzNDIyMjU4LCJleHAiOjIwMTg3ODIyNTgsImF1ZCI6ImdmdyIsImlzcyI6ImdmdyJ9.bFXXaIHSGeVW6jnhaKh32QTthpg6_IjU9xKG7eXp6KVze3fZVFZlqZDUQ0gM73KfxdzqGX8RLJj0eMU-37Vm-UDqSRhq2YbuceUK8GfNYvZL6kDXk7dN3Oi0MGVQk4VFLR5GXiItqZCl4eYFeCvUHD9ZLoFD_qtmKF3Ertyp2HgGvqUPuOPhu-_JCwi-mYfyGHTBKmHPE0iJtKoUVDBKlPXKzdca_QHG0E1fSAcvebJ2UTCCuYf8SMwSg5lBhqJ_kNNYz5PdsBEunpVCFv8vUtFoQe9ZcvdjCSasuk0zPP4_bX_PWUSUdIMAqAGQRHBbjG3HFKE8FfbHukM6FdvzbCE5Xl7oglw4C5i5QuAHUVs0Ez0rTg5Ww95hzWF09h3wgRuqspYDzLyiT2KgXhmw2paVO3QssiDBhSfavEgY_kz2KhVaxaxosCuXJ3p_aJWIiGHTcknzC_sdNB4EJSaQpFJ-WAkepjF0CWPyJsUcOkPyxN-AOzshB4uYpCzl3tou";

		//sekcja odpowiedzialna za ogarnianie zapytan typu POST

		/*

		String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/4wings/generate-png";
	        Map<String, String> parameters = Map.of(
	                "interval", "10DAYS",
	                "datasets[0]", "public-global-fishing-effort:latest",
	                "color", "#361c0c",
	                "date-range", "2020-01-01,2020-01-31"
	                // Add other parameters as needed
	        );
	        
	        // Add the 'datasets' parameter to the URL
	        String queryString = buildQueryString(parameters);
	        URI uri = new URI(apiUrl + queryString);

	        // Build the POST request
	        HttpRequest postRequest = HttpRequest.newBuilder()
	                .uri(uri)
	                .header("Authorization", "Bearer " + apiToken)
	                .header("Content-Type", "application/x-www-form-urlencoded")
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();

	        // Send the POST request and handle the response
	        HttpClient httpClient = HttpClient.newHttpClient();
	        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

	        // Process the response
	        System.out.println("Response Code: " + postResponse.statusCode());
	        System.out.println("Response Body: " + postResponse.body());
		
	        Gson gson= new Gson();
	        
	        Transcript transcript= new Transcript();
	        
	        transcript= gson.fromJson(postResponse.body(), Transcript.class);
	        
	        System.out.println(transcript.getUrl());
	       
	        
	        //kolejna faza zapytania
	        
	        String apiUrl2 = "https://gateway.api.globalfishingwatch.org/v3/4wings/tile/heatmap/2/3/1";
	        Map<String, String> parameters2 = Map.of(
	                "format", "PNG",
	                "interval", "10DAYS",
	                "datasets[0]", "public-global-fishing-effort:latest",
	                "date-range", "2020-01-01,2020-01-31",
	                "style", transcript.getUrl()
	                // Add other parameters as needed
	        );
	        
	        String queryString2 = buildQueryString(parameters2);
	        URI uri2 = new URI(apiUrl2 + queryString2);
	        
	        HttpRequest getRequestNext = HttpRequest.newBuilder()
	                .uri(uri2)
	                .header("Authorization", "Bearer " + apiToken)
	                .GET()
	                .build();
	        
	        
	        HttpClient httpClient2 = HttpClient.newHttpClient();
	        HttpResponse<byte[]> getResponse = httpClient2.send(getRequestNext, HttpResponse.BodyHandlers.ofByteArray());

	        // Save the response to a file (tile-by-coordinates.PNG)
	        java.nio.file.Files.write(java.nio.file.Paths.get("tile-by-coordinates.PNG"), getResponse.body());

	        // Process the response
	        System.out.println("Response Code: " + getResponse.statusCode());
	        
	        //get to know why response code is 503
	        

	   */

		//example GET request

		//section typical get by ID


		//HERE GOES COMMENTED CODE
		
		/*
		
	    String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/vessels/search";

		Map<String, String> parameters = Map.of(
		        "where", "flag = 'KOR'",
		        "datasets[0]", "public-global-vessel-identity:latest",
		        // Ensure 'includes' is passed as an array
		        "includes[0]", "MATCH_CRITERIA",
		        "includes[1]", "OWNERSHIP",
		        "includes[2]", "AUTHORIZATIONS",
		        "limit", "1"
		);
		
		
		
        
        String queryString = encodeParams(parameters);
        
        URI uri = new URI(apiUrl + "?" + queryString);
        

        
        
        HttpRequest getRequest = HttpRequest.newBuilder()
               .uri(uri)
                .header("Authorization", "Bearer " + apiToken)
                .GET()
                .build();
        
        
       
        
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Response Code: " + response.statusCode());
        
        
        
        System.out.println("Response Body: " + response.body());
        
        
        
        //printing json in more readable way
        
        // Create a Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Parse the JSON string to a JsonElement
        JsonElement jsonElement = JsonParser.parseString(response.body());

        // Pretty print the JsonElement
        String prettyJsonString = gson.toJson(jsonElement);

        // Print the pretty-printed JSON
        System.out.println(prettyJsonString);
       
		
        
        
        */
		
		
		
		
		/*
		
		VesselsGETDeclaration getDeclaration= new VesselsGETDeclaration();

		getDeclaration.addLimit("2");
		getDeclaration.addFlag("'KOR'");

		Map<String, String> parameters= getDeclaration.getParametersMap();

		HttpRequest httpRequest= getDeclaration.createGETRequest(parameters);

		HttpResponse<String> response= getDeclaration.sendGETRequest(httpRequest);

		System.out.println(response.statusCode());
		System.out.println(response.body());
		
		
		Response respons= VesselsGETDeclarationToObjects.convertToObject(response.body());
		
		System.out.println(respons.getEntries().get(0).getRegistryInfo().get(0).getShipname());
		System.out.println(respons.getEntries().get(0).getRegistryOwners().get(0).getName());
		
		System.out.println(respons.getEntries().get(1).getRegistryInfo().get(0).getShipname());
		System.out.println(respons.getEntries().get(1).getRegistryOwners().get(0).getName());
		
		System.out.println(respons.getEntries().get(0).getCombinedSourcesInfo().get(0).getGeartypes());
		System.out.println(respons.getEntries().get(0).getRegistryPublicAuthorizations().get(0).getSsvid());
		
		System.out.println(respons.getEntries().get(1).getCombinedSourcesInfo().get(0).getGeartypes());
		System.out.println(respons.getEntries().get(1).getRegistryPublicAuthorizations().get(0).getSsvid());
		
		
		*/

		/*

		VesselsInsightsPOSTDeclaration postDeclaration = new VesselsInsightsPOSTDeclaration();
		postDeclaration.addIncludes(IncludesValuesInsightsAPI.GAP);
		postDeclaration.addDate("2022-07-11", "2023-07-11");
		postDeclaration.addVessels("2339c52c3-3a84-1603-f968-d8890f23e1ed");

		JsonObject jsonBodyObject = postDeclaration.getJsonBodyObject();
		HttpRequest httpRequest = postDeclaration.createPOSTRequest(jsonBodyObject);
		HttpResponse<String> response = postDeclaration.sendPOSTRequest(httpRequest);

		System.out.println(response.statusCode());
		System.out.println(response.body());

		InsightsPeriodGap insightsPerdiodGap = VesselsInsightsPOSTDeclarationToObjects.convertToObject(response.body());

		System.out.println(insightsPerdiodGap.getGap().getDatasets()[0]);
		System.out.println(insightsPerdiodGap.getGap().getAisOff()[0]);
		
		*/
		

		
//		FourWingsPOSTMapDeclaration postDeclaration= new FourWingsPOSTMapDeclaration();
//		postDeclaration.addInterval(IntervalValues4WingsApi._10DAYS);
//		postDeclaration.addDateRange("2020-01-01", "2020-01-31");
//		postDeclaration.addColor("#303030");
//
//		HttpRequest postRequest= postDeclaration.createPOSTRequest(postDeclaration.getParametersMap());
//
//		HttpResponse<String> response= postDeclaration.sendPOSTRequest(postRequest);
//
//		System.out.println(response.statusCode());
//		System.out.println(response.body());
//
//		StyleConfiguration styleConfiguration= FourWingsPOSTMapDeclarationToObjects.convertToObject(response.body());
//		System.out.println(styleConfiguration.getUrl());
//
//		String urlNeeded= styleConfiguration.getUrl();
//
//		FourWingsGETDeclaration getRequest= new FourWingsGETDeclaration(urlNeeded, 2, 3, 1);
//
//		System.out.println(getRequest.getChangedURL());
//
//		HttpRequest httpRequest= getRequest.createGETRequest();
//
//		HttpResponse<byte[]> anotherResponse= getRequest.sendGETRequest(httpRequest);
//
//
//		String contentType = anotherResponse.headers().firstValue("Content-Type").orElse("");
//		System.out.println("Content-Type: " + contentType);
//
//		byte[] responseBytes = anotherResponse.body();
//
//		String fileName="zdjecie5.png";
//		getRequest.savePNG(responseBytes, fileName);


//		EventsGETDeclaration getDeclaration= new EventsGETDeclaration();
//
//		getDeclaration.addVesel("914f83946-6af4-04c6-4974-44a203a87952");
//
//
//
//		Map<String, String> parameters= getDeclaration.getParametersMap();
//
//		HttpRequest httpRequest= getDeclaration.createGETRequest(parameters);
//
//		HttpResponse<String> response= getDeclaration.sendGETRequest(httpRequest);
//
////		System.out.println(response.statusCode());
////		System.out.println(response.body());
//
//		Root root= EventsGETDeclarationToObjects.convertToObject(response.body());
//
//		System.out.println(root.entries.get(0).position.lat);
//		System.out.println(root.entries.get(0).position.lon);
		//System.out.println(respons.getEntries().get(0).getRegistryOwners().get(0).getName());









		
		//getRequest.savePNG(responseBytes, "tile-by-coordinatesNew.png");



		//testing whether getting reports works okey
	
		
		/*
		
		double[][] coordinates = {
	            {-76.11328125, -26.273714024406416},
	            {-76.201171875, -26.980828590472093},
	            {-76.376953125, -27.527758206861883},
	            {-76.81640625, -28.30438068296276},
	            {-77.255859375, -28.767659105691244},
	            {-77.87109375, -29.152161283318918},
	            {-78.486328125, -29.45873118535532},
	            {-79.189453125, -29.61167011519739},
	            {-79.892578125, -29.6880527498568},
	            {-80.595703125, -29.61167011519739},
	            {-81.5625, -29.382175075145277},
	            {-82.177734375, -29.07537517955835},
	            {-82.705078125, -28.6905876542507},
	            {-83.232421875, -28.071980301779845},
	            {-83.49609375, -27.683528083787756},
	            {-83.759765625, -26.980828590472093},
	            {-83.84765625, -26.35249785815401},
	            {-83.759765625, -25.64152637306576},
	            {-83.583984375, -25.16517336866393},
	            {-83.232421875, -24.447149589730827},
	            {-82.705078125, -23.966175871265037},
	            {-82.177734375, -23.483400654325635},
	            {-81.5625, -23.241346102386117},
	            {-80.859375, -22.998851594142906},
	            {-80.15625, -22.917922936146027},
	            {-79.453125, -22.998851594142906},
	            {-78.662109375, -23.1605633090483},
	            {-78.134765625, -23.40276490540795},
	            {-77.431640625, -23.885837699861995},
	            {-76.9921875, -24.28702686537642},
	            {-76.552734375, -24.846565348219727},
	            {-76.2890625, -25.48295117535531},
	            {-76.11328125, -26.273714024406416}
	        };
		
		
		FourWingsPOSTReportDeclaration report= new FourWingsPOSTReportDeclaration();
		
		report.addSpatialResolution(SpatialResolution4WingsApi.LOW);
		report.addTemporalResolution(TemporalResolution4WingsApi.MONTHLY);
		report.addGroupBy(GroupByValues4WingsApi.VESSEL_ID);
		report.addDateRange("2022-01-01", "2022-05-01");
		report.addFormat(FormatValues4WingsApi.JSON);
		report.addRegionBODY("public-eez-areas", 5690, null, null, "");
		//report.addGeoJsonBODY(convertToCoordinatesList(coordinates));
		
		
		HttpRequest requests= report.createPOSTRequest(report.getParametersMap(), report.getJsonBodyObject());
		
		HttpResponse<String> anotherResponse= report.sendPOSTRequest(requests);
		
		pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.FishingData responseproblematic= 
				FourWingsPOSTReportDeclarationToObjects.convertToObject(anotherResponse.body());
		
		Map<String, List<Entry>> firstEntryGroup = responseproblematic.getEntries().get(0);
		
		List<Entry> entries = firstEntryGroup.get("public-global-fishing-effort:v20231026");
		
		System.out.println(entries.get(0).getFlag());
		System.out.println(entries.get(0).getGeartype());
		System.out.println(entries.get(0).getEntryTimestamp());
		
		
		*/

		/*
		
		//testing 4wings stats API
		FourWingsStatsGETDeclaration statsDeclaration= new FourWingsStatsGETDeclaration();
		
		Set<FieldsValues4WingsStatsApi> fields= new HashSet<>();
		fields.add(FieldsValues4WingsStatsApi.FLAGS);
		fields.add(FieldsValues4WingsStatsApi.VESSEL_IDS);
		fields.add(FieldsValues4WingsStatsApi.ACTIVITY_HOURS);
		
		statsDeclaration.addFields(fields);
		statsDeclaration.addDate("2022-10-22", "2023-01-22");
		
		HttpRequest request= statsDeclaration.createGETRequest(statsDeclaration.getParametersMap());
		
		HttpResponse<String> response= statsDeclaration.sendGETRequest(request);
		
		System.out.println(response.statusCode());
		System.out.println(response.body());
		
		WorldwideStats worldWideStats= FourWingsStatsGETDeclarationToObjects.convertToObject(response.body());
		
		System.out.println(worldWideStats.getMaxLat());
		
	}
	
	private static List<List<Double>> convertToCoordinatesList(double[][] coordinates) {
        List<List<Double>> coordinatesList = new ArrayList<>();

        for (double[] coordinate : coordinates) {
            List<Double> point = new ArrayList<>();
            for (double value : coordinate) {
                point.add(value);
            }
            coordinatesList.add(point);
        }

        return coordinatesList;
    }
	
	private static void saveToFile(byte[] data, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }
	
	private static List<Transcript> parseJsonString(String jsonString) {
        try {
            // Define the type of the list of YourClass
            Type listType = new TypeToken<List<Transcript>>() {}.getType();

            // Use Gson to parse the JSON string into a list of YourClass objects
            return new Gson().fromJson(jsonString, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
	
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
	
	
	
	private static String encodeParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }
	
	private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

	*/
	}

}
