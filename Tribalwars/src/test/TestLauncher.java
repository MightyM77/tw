package test;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.Calendar;

import config.Configuration;
import utile.GameHelper;
import utile.Troop;

public class TestLauncher {

	public static void main(String[] args) {
		try {
			while (true) {
				int i = Integer.valueOf("a");
			}
		} catch (Exception e) {
			e.printStackTrace();
			while (true) {
				Toolkit.getDefaultToolkit().beep();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}