package tool.farmassistant;

public enum FarmButton {
	A (0),
	B (1),
	C (2);
	
	final int index;
	
	private FarmButton(int pIndex) {
		this.index = pIndex;
	}
	
	public static FarmButton valueOf(FarmTemplate farmTemplate) {
		return valueOf(farmTemplate.name());
	}
	
	public int getIndex() {
		return this.index;
	}
}