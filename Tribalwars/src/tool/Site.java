package tool;

import java.awt.Point;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utile.GoogleMail;
import utile.ResourceBundleUtil;
import config.TwConfiguration;

public class Site {

	private final String file;
	private final TwConfiguration config;
	
	protected final Map<String, String> urlParameters = new HashMap<String, String>();

	protected SimpleDateFormat someDateFormat = new SimpleDateFormat();

	public Site(TwConfiguration pConfig, String pFile, String screen) {
		this.config = pConfig;
		this.file = pFile;
		if (screen.length() > 0) {
			this.urlParameters.put("screen", screen);
		}
	}
	
	protected TwConfiguration config() {
		return config;
	}
	
	protected WebDriver driver() {
		return config.getDriver();
	}

	public int getIncomingAmount() {
		WebElement incomingAmountSpan = findElement(By.id("incomings_amount"));
		return Integer.valueOf(incomingAmountSpan.getText().replaceAll("[^0-9]", ""));
	}

	public void goToSite() {

		String url = "http://" + config().getLocale().getCountry().toLowerCase() + config.getWorld() + "." + ResourceBundleUtil.getGeneralBundleString("hostname", config.getLocale()) + this.file + "?";

		Iterator<Entry<String, String>> iterator = urlParameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) iterator.next();
			url += pairs.getKey() + "=" + pairs.getValue() + "&";
		}
		url = url.substring(0, url.length());
		this.driver().get(url);
	}

	public Map<String, String> getCurrentUrlsParameters() {
		Map<String, String> currentUrlParameters = new HashMap<String, String>();
		String[] currentUrlParametersString = driver().getCurrentUrl().split("\\?")[1].split("&");
		String[] keyVal;
		for (String parameter : currentUrlParametersString) {
			keyVal = parameter.split("=");
			currentUrlParameters.put(keyVal[0], keyVal[1]);
		}
		return currentUrlParameters;
	}

	private void checkForBotProtection() {
		List<WebElement> botElement = driver().findElements(By.id("bot_check"));

		int durchlaeufe = 0;
		while (botElement.size() > 0) {
			if (botElement.get(0).getCssValue("display").equals("none")) {
				break;
			}
			if (durchlaeufe < 1) {
				TwConfiguration.LOGGER.warn("!!!!!!!!!!!!!!! BOTPROTECTION AUFGETAUCHT !!!!!!!!!!!!!!!");
				if (config().isEmailOnBotProtection()) {
					try {
						GoogleMail.Send(config().getSenderEmail(), config().getSenderPw(), "PROTECTION", "Protection aufgetaucht", config().getRecipientEmails());
					} catch (AddressException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
			botElement = driver().findElements(By.id("bot_check"));

			if (config().isBingOnBotProtection()) {
				Toolkit.getDefaultToolkit().beep();
			}

			durchlaeufe++;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (durchlaeufe > 0) {
			TwConfiguration.LOGGER.warn("Botprotection wurde eingegeben, fahre fort mit Programmablauf");
		}
	}

	protected WebElement findElement(By by) {
		checkForBotProtection();
		WebElement results = null;
		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			results = driver().findElement(by);
			if (results.isDisplayed()) {
				break;
			}
		}
		return results;
	}

	protected List<WebElement> findElements(By by) {
		checkForBotProtection();
		List<WebElement> results = null;
		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			results = driver().findElements(by);
			if (results.size() > 0) {
				break;
			}
		}
		return results;
	}

	public int getWood() {
		return Integer.valueOf(findElement(By.id("wood")).getText());
	}

	public int getWoodProductionPerHour() {
		return Integer.valueOf(findElement(By.id("wood")).getAttribute("title").replaceAll("[^0-9]", ""));
	}

	public int getClay() {
		return Integer.valueOf(findElement(By.id("clay")).getText());
	}

	public int getClayProductionPerHour() {
		return Integer.valueOf(findElement(By.id("clay")).getAttribute("title").replaceAll("[^0-9]", ""));
	}

	public int getIron() {
		return Integer.valueOf(findElement(By.id("iron")).getText());
	}

	public int getIronProductionPerHour() {
		return Integer.valueOf(findElement(By.id("iron")).getAttribute("title").replaceAll("[^0-9]", ""));
	}

	public int getCurrenPopulation() {
		return Integer.valueOf(findElement(By.id("pop_current_label")).getText());
	}

	public int getMaxPopulation() {
		return Integer.valueOf(findElement(By.id("pop_max_label")).getText());
	}

	public int getRankPoints() {
		return Integer.valueOf(findElement(By.id("rank_points")).getText());
	}

	public int getRank() {
		return Integer.valueOf(findElement(By.id("rank_rank")).getText().replaceAll("[^0-9]", ""));
	}

	public Point getCurrentCoord() {
		String text = findElement(By.xpath("//tr[@id='menu_row2']/td[@class='box-item']/b[@class='nowrap']")).getText();
		text = text.substring(1, 8);
		String[] splitText = text.split("\\|");
		return new Point(Integer.valueOf(splitText[0]), Integer.valueOf(splitText[1]));
	}
}
