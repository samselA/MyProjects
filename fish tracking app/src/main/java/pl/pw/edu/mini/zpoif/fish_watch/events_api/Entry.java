package pl.pw.edu.mini.zpoif.fish_watch.events_api;

import java.util.List;

public class Entry {
    public String start;
    public String end;
    public String id;
    public String type;
    public Position position;
    public Regions regions;
    public List<Double> boundingBox;
    public Distances distances;
    public Vessel vessel;
    public PortVisit port_visit;


}
