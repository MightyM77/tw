package tool.farmassistant;

public enum FarmTemplate {
	A (0),
	B (1);
	
	final int index;
	
	private FarmTemplate(int pIndex) {
		this.index = pIndex;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public FarmButton convertToFarmButton() {
		for (FarmButton fb : FarmButton.values()) {
			if (fb.name().equals(name())) {
				return fb;
			}
		}
		return null;
	}
}
