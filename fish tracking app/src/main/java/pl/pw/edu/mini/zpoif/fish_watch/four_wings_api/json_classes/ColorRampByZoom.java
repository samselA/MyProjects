package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes;

import java.util.List;
import java.util.Map;

public class ColorRampByZoom {
	private Map<String, List<ColorRampStep>> stepsByZoom;

	public Map<String, List<ColorRampStep>> getStepsByZoom() {
		return stepsByZoom;
	}
	
}
