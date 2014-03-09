package utile;

import java.util.HashMap;
import java.util.Map;

public class TroopTemplate {

	private final Map<Troop, Integer> troops;
	
	public TroopTemplate() {
		this(getZeroTroopTemplate());
	}
	
	public TroopTemplate(Map<Troop, Integer> pTroops) {
		for (Troop troop : Troop.values()) {
			if (!pTroops.containsKey(troop)) {
				pTroops.put(troop, 0);
			}
		}
		this.troops = pTroops;
		
	}
	
	public boolean lessOrEqualTroops(TroopTemplate availableTroops) {
		boolean enoughTroops = true;
		Map<Troop, Integer> availableTroopsMap = availableTroops.getAllTroops();
		for (Troop troop : availableTroopsMap.keySet()) {
			if (troops.get(troop) > availableTroopsMap.get(troop)) {
				enoughTroops = false;
			}
		}
		return enoughTroops;
	}
	
	/**
	 * @return eine Troop HashMap mit Wert 0 bei allen Truppen
	 */
	private static Map<Troop, Integer> getZeroTroopTemplate() {
		Map<Troop, Integer> troops = new HashMap<Troop, Integer>();
		for (Troop troop : Troop.values()) {
			troops.put(troop, 0);
		}
		return troops;
	}
	
	public Map<Troop, Integer> getAllTroops() {
		return this.troops;
	}
	
	public Map<Troop, Integer> getNotZeroTroops() {
		Map<Troop, Integer> notZeroTroops = new HashMap<Troop, Integer>();
		for (Troop troop : troops.keySet()) {
			if (troops.get(troop) > 0) {
				notZeroTroops.put(troop, getAllTroops().get(troop));
			}
		}
		return notZeroTroops;
	}
	
	public Troop getSlowestTroop() {
		Map<Troop, Integer> notZeroTroops = getNotZeroTroops();
		Troop[] troops = new Troop[notZeroTroops.size()];
		return Troop.getSlowestTroop(getNotZeroTroops().keySet().toArray(troops));
	}
	
	public void setTroops(Troop troop, int amount) {
		getAllTroops().put(troop, amount);
	}
	
	public void addTroopTroops(Troop troop, int amount) {
		getAllTroops().put(troop, getAllTroops().get(troop) + amount);
	}
	
	public void subtractTroopTemplate(TroopTemplate troopTemplate) {
		Map<Troop, Integer> otherTroops = troopTemplate.getAllTroops();
		for (Troop troop : otherTroops.keySet()) {
			int newValue = troops.get(troop) - otherTroops.get(troop);
			this.troops.put(troop, newValue);
		}
	}
}