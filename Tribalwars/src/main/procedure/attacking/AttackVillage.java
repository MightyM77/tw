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
	private final Point targetCoords;
	private final int sourceVillageId;
	private final boolean attackWithAllTroops;
	
	private AttackVillage (TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopsAmount, int pSourceVillageId, Point pTargetCoords, boolean pAttackWithAllTroops) {
		super(pConfig, pActivationTime.getTimeInMillis());
		this.place = pPlace;
		this.troopsAmount = pTroopsAmount;
		this.sourceVillageId = pSourceVillageId;
		this.targetCoords = pTargetCoords;
		this.attackWithAllTroops = pAttackWithAllTroops;
	}
	
	public AttackVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopsAmount, int pSourceVillageId, Point pTargetCoords) {
		this(pConfig, pPlace, pActivationTime, pTroopsAmount, pSourceVillageId, pTargetCoords, false);
	}

	public AttackVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, int pSourceVillageId, Point pTargetCoords) {
		this(pConfig, pPlace, pActivationTime, null, pSourceVillageId, pTargetCoords, true);
	}
	
	private Place place() {
		return this.place;
	}
	 
	private int getSourceVillageId() {
		return this.sourceVillageId;
	}
	
	private Point getTargetCoords() {
		return this.targetCoords;
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
				TwConfiguration.LOGGER.info("Greife ({}|{}) mit allen Truppen an", this.targetCoords.x, this.targetCoords.y);
				place().attackWithAllTroops(this.sourceVillageId, this.targetCoords);
			} else {
				TwConfiguration.LOGGER.warn("Es sind keine Truppen anwesend, ({}|{}) kann nicht mit allen Truppen angegriffen werden", this.targetCoords.x, this.targetCoords.y);
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
				TwConfiguration.LOGGER.info("Greife ({}|{}) an", this.targetCoords.x, this.targetCoords.y);
				place().attack(troopsAmount(), getTargetCoords());
			} else {
				TwConfiguration.LOGGER.warn("Es sind nicht genügend Truppen anwesend, ({}|{}) kann nicht angegriffen werden", this.targetCoords.x, this.targetCoords.y);
			}
		}
		
		return procedures;
	}

}
