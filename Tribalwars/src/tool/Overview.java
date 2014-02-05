package tool;

public class Overview extends Site {

	private static final Overview INSTANCE = new Overview();
	
	private Overview() {
		super("/game.php", "overview");
	}
	
	public static Overview getInstance() {
		return Overview.INSTANCE;
	}

}
