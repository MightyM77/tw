package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import main.ResourceBundleUtil;
import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestLauncher {

	public static void main(String[] args) {		
		String currentUrlParametersStringa = "http://us13.tribalwars.us/game.php?village=59806&order=date&dir=desc&Farm_page=0&screen=am_farm";

		
		
		Map<String, String> currentUrlParameters = new HashMap<String, String>();
		String[] currentUrlParametersString = currentUrlParametersStringa.split("\\?")[1].split("&");
		String[] keyVal;
		for (String parameter : currentUrlParametersString) {
			keyVal = parameter.split("=");
			currentUrlParameters.put(keyVal[0], keyVal[1]);
		}

		System.out.println(currentUrlParameters.toString());
	}

}
