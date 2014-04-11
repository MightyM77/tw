package tool.overview_villages.incoming;

import config.TwConfiguration;

public class IncomingAttack extends Incoming {
	
	public IncomingAttack(TwConfiguration pConfig) {
		super(pConfig, Mode.ATTACKS);
	}

}