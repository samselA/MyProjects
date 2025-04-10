package pl.pw.edu.mini.zpoif.fish_watch.events_api;

import com.google.gson.Gson;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes.Response;

public class EventsGETDeclarationToObjects {

    /**
     * @param jsonString, String used to convert JSON string to object
     * @return response, object representation of converted JSON string
     */

    public static Root convertToObject(String jsonString) {

        Gson gson = new Gson();
        Root response = gson.fromJson(jsonString, Root.class);

        return response;
    }


}
