package utile;

public enum Resource {

	WOOD ("wood"),
	CLAY ("stone"),
	IRON ("iron");
	
	private final String id;
	
	private Resource(String pId) {
		this.id = pId;
	}
	
	public String getId() {
		return this.id;
	}
}
