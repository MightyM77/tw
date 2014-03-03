package main.procedure.attacking;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.procedure.Procedure;

import org.openqa.selenium.WebDriver;

import config.TwConfiguration;
import tool.Place;
import utile.GameHelper;
import utile.Troop;
import utile.TroopTemplate;

public class AttackVillage extends Procedure {

	private final Place place;
	private final TroopTemplate troopsAmount;
	private final Point coords;
	private final boolean attackWithAllTroops;
	
	private AttackVillage (TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopsAmount, Point pCoords, boolean pAttackWithAllTroops) {
		super(pConfig, pActivationTime);
		this.place = pPlace;
		this.troopsAmount = pTroopsAmount;
		this.coords = pCoords;
		this.attackWithAllTroops = pAttackWithAllTroops;
	}
	
	public AttackVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopsAmount, Point pCoords) {
		this(pConfig, pPlace, pActivationTime, pTroopsAmount, pCoords, false);
	}

	public AttackVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, Point pCoords) {
		this(pConfig, pPlace, pActivationTime, null, pCoords, true);
	}
	
	private Place place() {
		return this.place;
	}
	 
	private Point coords() {
		return this.coords;
	}
	
	private Map<Troop, Integer> troopsAmount() {
		return this.troopsAmount.getAllTroops();
	}
	
	@Override
	public List<Procedure> doAction() {
		TwConfiguration.LOGGER.info("Starte Dorf-Angreifen-Prozedur");
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		place().goToSite();
		
		if (this.attackWithAllTroops) {
			if (place().isSomeUnitPresent()) {
				TwConfiguration.LOGGER.info("Greife ({}|{}) mit allen Truppen an", this.coords.x, this.coords.y);
				place().attackWithAllTroops(this.coords);
			} else {
				TwConfiguration.LOGGER.warn("Es sind keine Truppen anwesend, ({}|{}) kann nicht mit allen Truppen angegriffen werden", this.coords.x, this.coords.y);
			}
		} else {
			boolean enoughTroops = true;
			Iterator<Entry<Troop, Integer>> iterator = troopsAmount().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
				if (place().getTroopAmount(pairs.getKey()) < pairs.getValue()) {
					enoughTroops = false;
				}
			}
			if (enoughTroops) {
				TwConfiguration.LOGGER.info("Greife ({}|{}) an", this.coords.x, this.coords.y);
				place().attack(troopsAmount(), coords());
			} else {
				TwConfiguration.LOGGER.warn("Es sind nicht genügend Truppen anwesend, ({}|{}) kann nicht angegriffen werden", this.coords.x, this.coords.y);
			}
		}
		
		return procedures;
	}

}
