package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters;

import com.google.gson.Gson;

import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.StyleConfiguration;

public class FourWingsPOSTMapDeclarationToObjects {
	/**
	 * @param jsonString, String used to convert JSON string to object
	 * @return insightsPeriodGap, object representation of converted JSON string
	 */
	
	public static StyleConfiguration convertToObject(String jsonString) {
		
		Gson gson = new Gson();
		StyleConfiguration styleConfiguration = gson.fromJson(jsonString, StyleConfiguration.class);
		
		return styleConfiguration;
	}
}
