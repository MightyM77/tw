package tool.farmassistant;

public enum FarmTemplate {
	A (0),
	B (1);
	
	final int index;
	
	private FarmTemplate(int pIndex) {
		this.index = pIndex;
	}
	
	public static FarmTemplate valueOf(FarmButton farmButton) {
		return valueOf(farmButton.name());
	}
	
	public int getIndex() {
		return this.index;
	}
}
