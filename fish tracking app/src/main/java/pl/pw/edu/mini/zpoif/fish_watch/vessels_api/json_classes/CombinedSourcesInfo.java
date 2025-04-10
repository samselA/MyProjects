package pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes;

import java.util.List;

public class CombinedSourcesInfo {
	 private String vesselId;
     private List<Geartype> geartypes;
     private List<Shiptype> shiptypes;

     public String getVesselId() {
		return vesselId;
	}


	public List<Geartype> getGeartypes() {
		return geartypes;
	}


	public List<Shiptype> getShiptypes() {
		return shiptypes;
	}

	public static class Geartype {
        
		private String name;
        private String source;
        private int yearFrom;
        private int yearTo;
		
        public String getName() {
			return name;
		}
		public String getSource() {
			return source;
		}
		public int getYearFrom() {
			return yearFrom;
		}
		public int getYearTo() {
			return yearTo;
		}
         
         
     }

     public static class Shiptype {
        
    	private String name;
        private String source;
        private int yearFrom;
        private int yearTo;
		
        public String getName() {
			return name;
		}
		public String getSource() {
			return source;
		}
		public int getYearFrom() {
			return yearFrom;
		}
		public int getYearTo() {
			return yearTo;
		}
         
         
     }
}
