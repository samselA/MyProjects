package pl.pw.edu.mini.zpoif.fish_watch.vessels_insights_api.json_classes;

public class InsightsPeriodGap {
	private Period period;
    private Gap gap;

    public static class Period {
        private String startDate;
        private String endDate;
		
        public String getStartDate() {
			return startDate;
		}
		public String getEndDate() {
			return endDate;
		}
    }

    public static class Gap {
        private String[] datasets;
        private String[] aisOff;
		
        public String[] getDatasets() {
			return datasets;
		}
		public String[] getAisOff() {
			return aisOff;
		}
        
    }

	public Period getPeriod() {
		return period;
	}

	public Gap getGap() {
		return gap;
	}
    
    
}
