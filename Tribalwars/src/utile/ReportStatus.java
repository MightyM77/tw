package utile;

/**
 * Die verschiedenen Filter (Radio-Boxen) die man auf der Berichtsseite auswählen kann.
 * filterName entspricht der ID der Radio-Boxen.
 * @author Mike_2
 */
public enum ReportStatus {

	SCOUT_ATTACK("blue", "filter_dots_blue"),
	NO_LOSSES("green", "filter_dots_green"),
	SOME_LOSSES("yellow", "filter_dots_yellow"),
	LOST_BUT_DAMAGED_BUILDINGS("red_yellow", "filter_dots_red_yellow"),
	LOST_BUT_SCOUTED("red_blue", "filter_dots_red_blue"),
	LOST("red", "filter_dots_red");
	
	private String color;
	private String filterId;
	
	private ReportStatus(String pColor, String pFilterId) {
		this.color = pColor;
		this.filterId = pFilterId;
	}
	
	public static ReportStatus stringContainsReportStatusColor(String color) {
		for (ReportStatus status : ReportStatus.values()) {
			if (color.contains(status.getColor())) {
				return status;
			}
		}
		return null;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getFilterId() {
		return this.filterId;
	}
}
