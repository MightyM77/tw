package tool;

import config.TwConfiguration;

public class VillageSite extends Site {

	public VillageSite(TwConfiguration pConfig, String screen) {
		super(pConfig, "/game.php", screen);
	}

	public void goToSite(int villageId) {
		this.urlParameters.put("village", String.valueOf(villageId));
		super.goToSite();
	}
}