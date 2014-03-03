package utile;

public class Resource {

	public static final String WOOD_ID = "wood";
	public static final String CLAY_ID = "stone";
	public static final String IRON_ID = "iron";
	
	private final int wood;
	private final int clay;
	private final int iron;
	
	public Resource(int pWood, int pClay, int pIron) {
		this.wood = pWood;
		this.clay = pClay;
		this.iron = pIron;
	}
	
	public int getWood() {
		return this.wood;
	}
	public int getClay() {
		return this.clay;
	}
	public int getIron() {
		return this.iron;
	}
	public int getTotal() {
		return this.wood+this.clay+this.iron;
	}
	public boolean biggerThan(Resource otherResource) {
		if (wood > otherResource.getWood() && clay > otherResource.getClay() && iron > otherResource.getIron()) {
			return true;
		}
		return false;
	}
}
