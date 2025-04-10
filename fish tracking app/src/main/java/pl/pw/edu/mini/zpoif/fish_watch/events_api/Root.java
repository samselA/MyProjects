package pl.pw.edu.mini.zpoif.fish_watch.events_api;

import java.util.List;

public class Root {
    public Metadata metadata;
    public int limit;
    public int offset;
    public int nextOffset;
    public int total;
    public List<Entry> entries;

}
