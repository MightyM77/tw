package utile;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

public enum Troop {

	SPEAR ("spear", 1080),
	SWORD ("sword", 1320),
	AXE ("axe", 1080),
	ARCHER ("archer", 1080),
	SPY ("spy", 540),
	LIGHT ("light", 600),
	MARCHER ("marcher", 600),
	HEAVY ("heavy", 660),
	PALADIN ("knight", 600);
	
	private String id;
	private int walkingDurationSeconds;
	
	private Troop(String pId, int pWalkingDurationSeconds) {
		this.id = pId;
		this.walkingDurationSeconds = pWalkingDurationSeconds;
	}
	
	public static Troop convertTroopIdToTroop(String troopId) {
		Troop finalTroop = null;
		for (Troop troop : Troop.values()) {
			if (troop.getId().equals(troopId)) {
				finalTroop = troop;
			}
		}

		assertThat("Die toopId '" + troopId + "' konnte nicht in ein Troop umgewandelt werden", finalTroop, notNullValue());
		return finalTroop;
	}
	
	public static Troop getSlowestTroop() {
		return getSlowestTroop(Troop.values());
	}
	
	public static Troop getSlowestTroop(Troop[] troops) {
		Troop slowestUnit = null;
		for (Troop troop : troops) {
			if (slowestUnit == null) {
				slowestUnit = troop;
			} else if (slowestUnit.getWalkingDurationSeconds() < troop.getWalkingDurationSeconds()) {
				slowestUnit = troop;
			}
		}
		return slowestUnit;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getWalkingDurationSeconds() {
		return this.walkingDurationSeconds;
	}
}
