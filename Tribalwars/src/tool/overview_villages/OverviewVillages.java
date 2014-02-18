package tool.overview_villages;

import tool.Site;

public class OverviewVillages extends Site {

	public OverviewVillages(String pMode) {
		super(".game.php", "overview_villages");
		this.urlParameters.put("mode", pMode);
	}

}
