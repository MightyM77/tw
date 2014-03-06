package config;

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import utile.ResourceBundleUtil;

public class TwConfiguration {

	public static final Logger LOGGER = LogManager.getRootLogger();

	private final String username;
	private final String password;
	private final int world;
	private final Locale locale;
	private final TimeZone timeZone;
	private final String senderEmail;
	private final String senderPw;
	private final String[] recipientEmails;
	private final WebDriver driver;
	private final boolean bingOnException;
	private final boolean bingOnBotProtection;
	private final boolean emailOnException;
	private final boolean emailOnBotProtection;

	public TwConfiguration(WebDriver pDriver, String propertyFilePath) throws ConfigurationException {
		PropertiesConfiguration mainConfig = new PropertiesConfiguration(propertyFilePath);
		mainConfig.setThrowExceptionOnMissing(true);

		PropertiesConfiguration localesConfig = new PropertiesConfiguration("locales.properties");
		
		
		// Initialize fields
		driver = pDriver;
		username = mainConfig.getString("username");
		password = mainConfig.getString("password");
		world = mainConfig.getInt("world");
		String[] localesString = localesConfig.getStringArray(mainConfig.getString("host"));
		locale = new Locale(localesString[0], localesString[1]);
		timeZone = TimeZone.getTimeZone(ResourceBundleUtil.getGeneralBundleString("timezone", locale));
		senderEmail = mainConfig.getString("email_notification.sender_email");
		senderPw = mainConfig.getString("email_notification.sender_pw");
		recipientEmails = mainConfig.getStringArray("email_notification.recipient_email");
		bingOnException = mainConfig.getBoolean("bing_on_exception");
		bingOnBotProtection = mainConfig.getBoolean("bing_on_bot_protection");
		emailOnException = mainConfig.getBoolean("email_on_exception");
		emailOnBotProtection = mainConfig.getBoolean("email_on_bot_protection");

		// Some general configuration
		TimeZone.setDefault(timeZone);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	public TwConfiguration(String propertyFilePath) throws ConfigurationException {
		this(new FirefoxDriver(), propertyFilePath);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getWorld() {
		return world;
	}

	public Locale getLocale() {
		return locale;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getSenderPw() {
		return senderPw;
	}

	public String[] getRecipientEmails() {
		return recipientEmails;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public boolean isBingOnException() {
		return bingOnException;
	}

	public boolean isBingOnBotProtection() {
		return bingOnBotProtection;
	}

	public boolean isEmailOnException() {
		return emailOnException;
	}

	public boolean isEmailOnBotProtection() {
		return emailOnBotProtection;
	}
}