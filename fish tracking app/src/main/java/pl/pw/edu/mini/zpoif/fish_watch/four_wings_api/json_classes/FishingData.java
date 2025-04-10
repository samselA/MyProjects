package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes;

import java.util.List;
import java.util.Map;

public class FishingData {
	private int total;
    private Integer limit;
    private Integer offset;
    private Integer nextOffset;
    private Metadata metadata;
    private List<Map<String, List<Entry>>> entries;
	
    public int getTotal() {
		return total;
	}
	public Integer getLimit() {
		return limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public Integer getNextOffset() {
		return nextOffset;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public List<Map<String, List<Entry>>> getEntries() {
		return entries;
	}
    
    
    
    
}
