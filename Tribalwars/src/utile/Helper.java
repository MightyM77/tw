package utile;

import org.openqa.selenium.WebElement;

public final class Helper {

	private static final Helper instance = new Helper();
	
	private Helper() {
		// singleton
	}
	
	public static Helper getInstance() {
		return instance;
	}
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
