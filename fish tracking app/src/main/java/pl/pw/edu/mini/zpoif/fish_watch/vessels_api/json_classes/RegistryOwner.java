package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class RegistryOwner {
	private String name;
    private String flag;
    private String ssvid;
    private List<String> sourceCode;
    private String dateFrom;
    private String dateTo;
    
	public String getName() {
		return name;
	}

	public String getFlag() {
		return flag;
	}

	public String getSsvid() {
		return ssvid;
	}

	public List<String> getSourceCode() {
		return sourceCode;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}
	
	
}
