package tool.overview_villages;

import tool.Site;
import config.TwConfiguration;

public class OverviewVillages extends Site {

	public OverviewVillages(TwConfiguration pConfig, String pMode) {
		super(pConfig, "/game.php", "overview_villages");
		this.urlParameters.put("mode", pMode);
	}

}
