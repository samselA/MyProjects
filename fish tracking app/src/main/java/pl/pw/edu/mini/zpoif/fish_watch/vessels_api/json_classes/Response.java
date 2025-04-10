package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class Response {
	private int limit;
    private String since;
    private int total;
    private List<Entry> entries;
    private Metadata metadata;
	
    public List<Entry> getEntries() {
		return entries;
	}

	public int getLimit() {
		return limit;
	}

	public String getSince() {
		return since;
	}

	public int getTotal() {
		return total;
	}

	public Metadata getMetadata() {
		return metadata;
	}
    
    
}
