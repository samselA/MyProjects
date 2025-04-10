package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

/**
 * The class is used for handling GET request from vessels API, it contains many methods for specific used cases
 */

public class VesselsGETDeclaration {

    /**
     * URI to connect to specific vessels API
     */

    private final String apiUrl = "https://gateway.api.globalfishingwatch.org/v3/vessels/search";

    /**
     * map for parameters in URI
     */

    private Map<String, String> parametersMap;

    public VesselsGETDeclaration() {
        parametersMap = new HashMap<>();

        parametersMap.put("datasets[0]", "public-global-vessel-identity:latest");
        parametersMap.put("includes[0]", "MATCH_CRITERIA");
        parametersMap.put("includes[1]", "OWNERSHIP");
        parametersMap.put("includes[2]", "AUTHORIZATIONS");
    }


    /**
     * @return returns parametersMap
     */
    public Map<String, String> getParametersMap() {
        return parametersMap;
    }

    /**
     *
     * @param httpRquest, created request to be send on server
     * @return returns HttpResponse in String format, contains elements such as: response code or response body
     */

    //this section is responsible for changing parameters


    /**
     * @param limit, add limit to parametersMap
     */

    public void addLimit(String limit) {
        parametersMap.put("limit", limit);
    }

    /**
     * @param shipName, add shipName to parametersMap
     */

    public void addShipName(String shipName) {
        //remember to put shipName in ''
        appendWhereParam("shipname=" + shipName);
    }

    /**
     * @param flag, add flag to parametersMap
     */

    public void addFlag(String flag) {
        //remember to put flag in ''
        appendWhereParam("flag=" + flag);
    }

    /**
     * @param to,   add transmission date to, to parametersMap
     * @param from, add transmission date from, to parametersMap
     */
    public void addTransmissionDate(String to, String from) {
        appendWhereParam("transmissionDateTo<=" + to);
        appendWhereParam("transmissionDateFrom>=" + from);
    }

    /**
     * @param mmsi, add mmsi to parametersMap
     */
    public void addMMSI(String mmsi) {
        //remember to put mmsi in ''
        appendWhereParam("mmsi=" + mmsi);
    }

    /**
     * @param ids, list of vessels to search
     */
    public void addIds(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            String counter = Integer.toString(i);
            parametersMap.put("ids[" + counter + "]", ids[i]);
        }
    }


    //end of section

    //beginning section of client request

    /**
     * @param httpRquest, request to get HTTP response
     * @return returns HTTP response, which might be later used to save object/objects
     */

    public HttpResponse<String> sendGETRequest(HttpRequest httpRquest) {

        HttpResponse<String> response = null;

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            response = httpClient.send(httpRquest, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
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
     * @param params, map which is transformed into String used in URL GET request
     * @return returns String used in URL GET request
     */

    private static String encodeParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
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

    private void appendWhereParam(String value) {
        if (!parametersMap.containsKey("where")) {
            parametersMap.put("where", value);
        } else {
            parametersMap.replace("where", parametersMap.get("where") + " AND " + value);
        }
    }

}
