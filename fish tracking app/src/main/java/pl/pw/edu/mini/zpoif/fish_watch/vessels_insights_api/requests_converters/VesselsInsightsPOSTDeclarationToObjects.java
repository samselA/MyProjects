package pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.requests_converters;

import com.google.gson.Gson;

import pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.json_classes.InsightsPeriodGap;

public class VesselsInsightsPOSTDeclarationToObjects {
	
	/**
	 * @param jsonString, String used to convert JSON string to object
	 * @return insightsPeriodGap, object representation of converted JSON string
	 */
	
	public static InsightsPeriodGap convertToObject(String jsonString) {
		
		Gson gson = new Gson();
		InsightsPeriodGap insightsPeriodGap = gson.fromJson(jsonString, InsightsPeriodGap.class);
		
		return insightsPeriodGap;
	}
}
