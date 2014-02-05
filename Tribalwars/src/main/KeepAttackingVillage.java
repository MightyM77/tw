package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

import tool.Place;
import utile.GameHelper;
import utile.Troop;

public class KeepAttackingVillage extends Procedure {

	private final Place place;
	private final Map<Troop, Integer> troopsAmount;
	private final Point coords;
	
	public KeepAttackingVillage(Calendar pActivationTime, Map<Troop, Integer> pTroopsAmount, Point pCoords) {
		super(pActivationTime);
		this.place = Place.getInstance();
		this.troopsAmount = pTroopsAmount;
		this.coords = pCoords;
	}

	private Place place() {
		return this.place;
	}
	 
	private Point coords() {
		return this.coords;
	}
	
	private Map<Troop, Integer> troopsAmount() {
		return this.troopsAmount;
	}
	
	@Override
	public List<Procedure> doAction() {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		place().goToSite();
		
		boolean enoughTroops = true;
		Iterator<Entry<Troop, Integer>> iterator = troopsAmount.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
			if (place().getTroopAmount(pairs.getKey()) < pairs.getValue()) {
				enoughTroops = false;
			}
		}
		
		Calendar troopsBack;
		if (enoughTroops) {
			place().attack(troopsAmount(), coords());
			troopsBack = Calendar.getInstance();
			Troop[] troops = new Troop[troopsAmount().keySet().size()];
			int walkingDuration = GameHelper.getInstance().getWalkingDurationSeconds(place().getCoords(), coords(), Troop.getSlowestTroop(troopsAmount.keySet().toArray(troops)));
			troopsBack.add(Calendar.SECOND, walkingDuration*2);
			// Wird benötigt da sonst die Zeit 2-3 Sekunden zu früh ausgerechnet wird, da der angriff manchmal noch laden muss
			troopsBack.add(Calendar.SECOND, 5);
		} else {
			troopsBack = Calendar.getInstance();
			troopsBack.add(Calendar.MINUTE, 5);
		}
		
		System.out.println(troopsBack.getTime());
		
		procedures.add(new KeepAttackingVillage(troopsBack, troopsAmount(), coords()));
		
		return procedures;
	}

}
