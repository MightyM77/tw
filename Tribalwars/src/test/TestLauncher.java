package test;

import java.util.Locale;


public class TestLauncher {

	public static void main(String[] args) {
		Locale test = Locale.US;
		System.out.println(test.getLanguage());
		System.out.println(test.getCountry());
	}
}