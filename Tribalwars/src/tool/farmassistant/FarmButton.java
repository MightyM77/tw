package tool.farmassistant;

public enum FarmButton {
	A (0),
	B (1),
	C (2);
	
	final int index;
	
	private FarmButton(int pIndex) {
		this.index = pIndex;
	}
	
	public int getIndex() {
		return this.index;
	}
}