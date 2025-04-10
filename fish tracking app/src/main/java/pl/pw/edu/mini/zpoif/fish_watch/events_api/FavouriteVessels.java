package pl.pw.edu.mini.zpoif.fish_watch.events_api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavouriteVessels {
    private List<String> favouriteVessels = new ArrayList<>();
    private List<Double> latitudes = new ArrayList<>();
    private List<Double> longtitude = new ArrayList<>();

    public FavouriteVessels(){
        String filePath = "output.txt";

        // Create an ArrayList to store the words
        ArrayList<String> wordsList = new ArrayList<>();

        // Read words from the file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Add each word to the ArrayList
                wordsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String word : wordsList) {
            this.addVesel(word);
        }
    }

    public void addVesel(String vesselId) {

        if(!this.favouriteVessels.contains(vesselId)){
            favouriteVessels.add(vesselId);

            EventsGETDeclaration getDeclaration = new EventsGETDeclaration();

            getDeclaration.addVesel(vesselId);
            Map<String, String> parameters = getDeclaration.getParametersMap();

            HttpRequest httpRequest = getDeclaration.createGETRequest(parameters);

            HttpResponse<String> response = getDeclaration.sendGETRequest(httpRequest);
            if(response.statusCode() != 200) {
                favouriteVessels.remove(vesselId);
                return;
            }
            Root root = EventsGETDeclarationToObjects.convertToObject(response.body());
            if(root.entries.isEmpty()) {
                favouriteVessels.remove(vesselId);
                return;
            }
            latitudes.add(root.entries.get(0).position.lat);
            longtitude.add(root.entries.get(0).position.lon);
        }

    }

    public void delVessel(int index){

        String name= favouriteVessels.get(index);
        double whatLat= latitudes.get(index);
        double whatLon= longtitude.get(index);

        latitudes.remove(whatLat);
        longtitude.remove(whatLon);
        favouriteVessels.remove(name);

    }

    public List<String> getFavouriteVessels() {
        return favouriteVessels;
    }

    public List<Double> getLongtitude() {
        return longtitude;
    }

    public List<Double> getLatitudes() {
        return latitudes;
    }
}
