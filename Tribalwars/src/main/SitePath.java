package main;

public enum SitePath {
	OVERVIEW ("/game.php?screen=overview");
	
	private String path;
	
	private SitePath(String pPath) {
		this.path = pPath;
	}
	
	public String getPath() {
		return this.path;
	}
}
