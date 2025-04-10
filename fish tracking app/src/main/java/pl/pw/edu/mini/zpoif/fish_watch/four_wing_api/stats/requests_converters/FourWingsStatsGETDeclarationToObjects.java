package pl.pw.edu.mini.zpoif.fish_watch.four_wing_api.stats.requests_converters;

import com.google.gson.Gson;

import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.stats.json_classes.WorldwideStats;

public class FourWingsStatsGETDeclarationToObjects {
	
	/**
	 * @param jsonString, String used to convert JSON string to object
	 * @return response, object representation of converted JSON string
	 */
	
	public static WorldwideStats convertToObject(String jsonString) {
		
		jsonString = jsonString.substring(1, jsonString.length() - 1);
		
		Gson gson = new Gson();
		WorldwideStats response = gson.fromJson(jsonString, WorldwideStats.class);
		
		return response;
	}
}
