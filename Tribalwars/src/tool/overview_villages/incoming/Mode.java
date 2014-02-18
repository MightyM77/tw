package tool.overview_villages.incoming;

public enum Mode {
	
	ALL ("all"),
	ATTACKS ("attacks"),
	SUPPORT ("supports");
	
	private String modeId;
	
	private Mode(String pModeId) {
		this.modeId = pModeId;
	}
	
	public String getModeId() {
		return this.modeId;
	}
}
