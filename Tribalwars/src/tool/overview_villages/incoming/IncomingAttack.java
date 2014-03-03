package tool.overview_villages.incoming;

import config.TwConfiguration;

public class IncomingAttack extends Incoming {
	
	private IncomingAttack(TwConfiguration pConfig) {
		super(pConfig, Mode.ATTACKS);
	}

}