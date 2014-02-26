package tool;

public class VillageSite extends Site {

	public VillageSite(String screen) {
		super("/game.php", screen);
	}

	public void goToSite(int villageId) {
		this.urlParameters.put("village", String.valueOf(villageId));
		super.goToSite();
		this.urlParameters.remove("village");
	}
}