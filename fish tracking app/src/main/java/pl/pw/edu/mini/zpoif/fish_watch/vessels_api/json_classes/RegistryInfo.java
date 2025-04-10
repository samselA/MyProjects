package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class RegistryInfo {
	private String id;
    private List<String> sourceCode;
    private String ssvid;
    private String flag;
    private String shipname;
    private String nShipname;
    private String callsign;
    private String imo;
    private boolean latestVesselInfo;
    private String transmissionDateFrom;
    private String transmissionDateTo;
    private List<String> geartypes;
    private double lengthM;
    private double tonnageGt;
    private String vesselInfoReference;
    
	public String getShipname() {
		return shipname;
	}
	public String getId() {
		return id;
	}
	public List<String> getSourceCode() {
		return sourceCode;
	}
	public String getSsvid() {
		return ssvid;
	}
	public String getFlag() {
		return flag;
	}
	public String getnShipname() {
		return nShipname;
	}
	public String getCallsign() {
		return callsign;
	}
	public String getImo() {
		return imo;
	}
	public boolean isLatestVesselInfo() {
		return latestVesselInfo;
	}
	public String getTransmissionDateFrom() {
		return transmissionDateFrom;
	}
	public String getTransmissionDateTo() {
		return transmissionDateTo;
	}
	public List<String> getGeartypes() {
		return geartypes;
	}
	public double getLengthM() {
		return lengthM;
	}
	public double getTonnageGt() {
		return tonnageGt;
	}
	public String getVesselInfoReference() {
		return vesselInfoReference;
	}
	
	
}
