package tool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import utile.Troop;
import config.TwConfiguration;

public class Place extends Site {
	
	private final static Random RANDOM_GENERATOR = new Random();
	
	public Place(TwConfiguration pConfig) {
		super(pConfig, "/game.php", "place");
	}
	
	private WebElement getTroopTd(Troop troop) {
		List<WebElement> troopInput = findElements(By.id("unit_input_" + troop.getId()));
		assertThat("Unit '" + troop.getId() + "' ist nicht verf�gbar", troopInput.toArray(), arrayWithSize(1));
		return troopInput.get(0).findElement(By.xpath(".."));
	}
	
	private WebElement getTroopInput(Troop troop) {
		return getTroopTd(troop).findElement(By.id("unit_input_" + troop.getId()));
	}
	
	private WebElement getTroopLink(Troop troop) {
		return getTroopTd(troop).findElements(By.tagName("a")).get(1);
	}

	private WebElement getInputX() {
		return findElement(By.id("inputx"));
	}
	
	private WebElement getInputY() {
		return findElement(By.id("inputy"));
	}
	
	private WebElement getAttackBtn() {
		return findElement(By.id("target_attack"));
	}
	
	private WebElement getSupportBtn() {
		return findElement(By.id("target_support"));
	}
	
	private WebElement getAllTroopsLink() {
		return findElement(By.id("selectAllUnits"));
	}
	
	private void setTroop(Troop troop, int amount) {
		getTroopInput(troop).sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(amount));
	}
	
	private void setTroops(Map<Troop, Integer> troopsAmount) {
		Iterator<Entry<Troop, Integer>> iterator = troopsAmount.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
			if (pairs.getValue() > 0) {
				setTroop(pairs.getKey(), pairs.getValue());
			}
		}
	}
	
	private void setCoords(Point coords) {
		getInputX().sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf((int) coords.getX()));
		getInputY().sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf((int) coords.getY()));
	}
	
	private WebElement getGoBtn() {
		return findElement(By.id("troop_confirm_go"));
	}
	
	private void attack() {
		getAttackBtn().click();
		getGoBtn().click();
	}
	
	private void support() {
		getSupportBtn().click();
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getGoBtn().click();
	}
	
 	public int getTroopAmount(Troop troop) {
		return Integer.valueOf(getTroopLink(troop).getText().replaceAll("[^0-9]", ""));
	}
	
 	// FIXME muss noch angepasst werden das es f�r alle Welten passt (da einige archer haben andere nicht)
 	public boolean isSomeUnitPresent() {
 		boolean someUnitPresent = false;
 		for (Troop troop : Troop.values()) {
 			if (troop != Troop.ARCHER && troop != Troop.MARCHER) {
 				if (getTroopAmount(troop) > 0) {
 					someUnitPresent = true;
 				}
 			}
 		}
 		return someUnitPresent;
 	}
 	
	public void attack(Map<Troop, Integer> troopsAmount, Point coords) {
		setTroops(troopsAmount);
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setCoords(coords);
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		attack();
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void attackWithAllTroops(int pSourceVillageId, Point targetCoords) {
		this.urlParameters.put("village", String.valueOf(pSourceVillageId));
		goToSite();
		getAllTroopsLink().click();
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setCoords(targetCoords);
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		attack();
	}

	public void support(Map<Troop, Integer> troopsAmount, Point coords) {
		setTroops(troopsAmount);
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setCoords(coords);
		try {
			Thread.sleep(500 + RANDOM_GENERATOR.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		support();
	}

	public void supportWithAllTroops(Point coords) {
		getAllTroopsLink().click();
		setCoords(coords);
		support();
	}
	
}
