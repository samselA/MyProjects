package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class RegistryPublicAuthorization {
	private String dateFrom;
    private String dateTo;
    private String ssvid;
    private List<String> sourceCode;
	
    public String getDateFrom() {
		return dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public String getSsvid() {
		return ssvid;
	}
	public List<String> getSourceCode() {
		return sourceCode;
	}
    
    
}
