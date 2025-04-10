package pl.pw.edu.mini.zpoif.fish_watch.events_api;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

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
import java.util.stream.Collectors;

public class EventsGETDeclaration {

    /**
     * URI to connect to specific vessels API
     */

    private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/events";

    /**
     * map for parameters in URI
     */

    private Map<String, String> parametersMap;

    public EventsGETDeclaration() {
        parametersMap= new HashMap<>();

        parametersMap.put("datasets[0]", "public-global-port-visits-c2-events:latest");
        parametersMap.put("limit", "1");
        parametersMap.put("offset", "0");
    }


    /**
     *
     * @return returns parametersMap
     */
    public Map<String, String> getParametersMap(){
        return parametersMap;
    }


    //this section is responsible for changing parameters



    /**
     * @param vessel, add vessels to parametersMap
     */
    public void addVesel(String vessel) {
        //remember to put mmsi in ''
        parametersMap.put("vessels[0]", vessel);
    }

//    /**
//     * @param ids, list of vessels to search
//     */
//    public void addIds(String[] ids) {
//        for(int i=0;i<ids.length;i++) {
//            String counter=Integer.toString(i);
//            parametersMap.put("ids["+counter+"]", ids[i]);
//        }
//    }


    //end of section

    //beginning section of client request

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
