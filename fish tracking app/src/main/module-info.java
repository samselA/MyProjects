module REST_fish_watch_application_official {
	
	exports pl.pw.edu.mini.zpoif.fish_watch.demonstrator;
	exports pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;
	exports pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.json_classes;
	exports pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes;
	exports pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.stats.json_classes;
	exports pl.pw.edu.mini.zpoif.fish_watch.events_api;


	opens pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes to com.google.gson;
	opens pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.json_classes to com.google.gson;
	opens pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes to com.google.gson;
	opens pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.stats.json_classes to com.google.gson;

	requires java.net.http;
	requires com.google.gson;

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.grsteamsaphics;
	requires org.jxmapviewer.jxmapviewer2;
	requires javafx.swing;
	exports pl.pw.edu.mini.zpoif.fish_watch.gui to javafx.graphics;

}