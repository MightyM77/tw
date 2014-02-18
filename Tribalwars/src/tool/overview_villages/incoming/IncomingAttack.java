package tool.overview_villages.incoming;

public class IncomingAttack extends Incoming {

	private static final IncomingAttack INSTANCE = new IncomingAttack();
	
	private IncomingAttack() {
		super(Mode.ATTACKS);
	}
	
	public static IncomingAttack getInstance() {
		return INSTANCE;
	}

}