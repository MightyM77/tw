package tool.overview_villages.incoming;

import config.TwConfiguration;

public class IncomingAll extends Incoming {
	
	private IncomingAll(TwConfiguration pConfig) {
		super(pConfig, Mode.ALL);
	}
	
}
