package tool.overview_villages.incoming;

public class IncomingSupport extends Incoming {

	private static final IncomingSupport INSTANCE = new IncomingSupport();
	
	private IncomingSupport() {
		super(Mode.SUPPORT);
	}
	
	public static IncomingSupport getInstance() {
		return INSTANCE;
	}

}
