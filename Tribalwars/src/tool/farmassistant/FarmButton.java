package tool.farmassistant;
import org.openqa.selenium.WebElement;

public class FarmButton {

	private WebElement farmButton;
	private boolean enabled;
	
	public FarmButton(WebElement pFarmButton, boolean pEnabled) {
		this.farmButton = pFarmButton;
		this.enabled = pEnabled;
	}
	
	public WebElement getFarmButton() {
		return this.farmButton;
	}
	
	public boolean enabled() {
		return this.enabled;
	}
}
