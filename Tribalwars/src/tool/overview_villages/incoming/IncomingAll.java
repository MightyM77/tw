package tool.overview_villages.incoming;

public class IncomingAll extends Incoming {

	private static final IncomingAll INSTANCE = new IncomingAll();
	
	private IncomingAll() {
		super(Mode.ALL);
	}

	public static IncomingAll getInstance() {
		return INSTANCE;
	}
	
}
