package utile;

public enum Building {
	HEADQUARTERS ("main"),
	BARRACKS ("barracks"),
	STABLE ("stable"),
	SMITHY ("smith"),
	REALLY_POINT ("plase"),
	STATUE ("statue"),
	MARKET ("market"),
	TIMBER_CAMP ("woord"),
	CLAY_PIT ("stone"),
	IRON_MINE ("iron"),
	FARM ("farm"),
	WAREHOUSE ("storage"),
	HIDING_PLACE ("hide"),
	WALL ("wall");
	
	private final String id;
	
	private Building(String pId) {
		this.id = pId;
	}
	
	public String getId() {
		return this.id;
	}
}
