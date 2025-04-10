package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters;

import com.google.gson.Gson;

import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes.Response;

public class VesselsGETDeclarationToObjects {
	
	/**
	 * @param jsonString, String used to convert JSON string to object
	 * @return response, object representation of converted JSON string
	 */
	
	public static Response convertToObject(String jsonString) {
		
		Gson gson = new Gson();
		Response response = gson.fromJson(jsonString, Response.class);
		
		return response;
	}
}
