package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class Entry {
	private String dataset;
    private int registryInfoTotalRecords;
    private List<RegistryInfo> registryInfo;
    private List<RegistryOwner> registryOwners;
    private List<CombinedSourcesInfo> combinedSourcesInfo;
    private List<RegistryPublicAuthorization> registryPublicAuthorizations;
    
	public List<CombinedSourcesInfo> getCombinedSourcesInfo() {
		return combinedSourcesInfo;
	}
	public List<RegistryPublicAuthorization> getRegistryPublicAuthorizations() {
		return registryPublicAuthorizations;
	}
	public List<RegistryInfo> getRegistryInfo() {
		return registryInfo;
	}
	public List<RegistryOwner> getRegistryOwners() {
		return registryOwners;
	}
	public String getDataset() {
		return dataset;
	}
	public int getRegistryInfoTotalRecords() {
		return registryInfoTotalRecords;
	}
	
	
}
