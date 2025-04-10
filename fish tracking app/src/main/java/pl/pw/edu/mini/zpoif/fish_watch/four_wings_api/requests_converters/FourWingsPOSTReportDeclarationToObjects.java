package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters;

import com.google.gson.Gson;

import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.FishingData;

public class FourWingsPOSTReportDeclarationToObjects {
	
	/**
	 * @param jsonString, String used to convert JSON string to object
	 * @return insightsPeriodGap, object representation of converted JSON string
	 */
	
	public static FishingData convertToObject(String jsonString) {
		
		Gson gson = new Gson();
		FishingData response = gson.fromJson(jsonString, FishingData.class);
		
		return response;
	}
}
