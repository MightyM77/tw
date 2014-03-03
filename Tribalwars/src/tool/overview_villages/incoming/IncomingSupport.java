package tool.overview_villages.incoming;

import config.TwConfiguration;

public class IncomingSupport extends Incoming {

	private IncomingSupport(TwConfiguration pConfig) {
		super(pConfig, Mode.SUPPORT);
	}
}
