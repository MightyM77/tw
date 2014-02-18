package main.procedure;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import main.procedure.attacking.AttackVillage;
import config.Configuration;
import tool.Market;
import tool.Overview;
import tool.overview_villages.incoming.IncomingAttack;

public class IncomingAttacksWatcher extends Procedure {

	private static final int MINUTES_BEFORE_INCOMING_ARRIVES = -3;
	private static final Point RAUSSTELL_COORDS = new Point(493,498);
	private final IncomingAttack ia = IncomingAttack.getInstance();
	
	public IncomingAttacksWatcher(Calendar pActivationTime) {
		super(pActivationTime);
	}

	@Override
	public List<Procedure> doAction() throws ParseException {
		Configuration.LOGGER.info("Starte Incoming-Attacks-Watcher-Procedur");
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		ia.goToSite();
		
		int incomingsCount = ia.getIncomingsCount();
		if (incomingsCount > 0) {
			for (int i = 0; i < incomingsCount; i++) {
				Calendar arrival = ia.getArrivingTime(i);
				Configuration.LOGGER.info("Angriff erkannt. Angreifer: {} Ankunftszeit: {}", ia.getAttackerName(i), ia.getArrivingTime(i).getTime());
				arrival.add(Calendar.MINUTE, MINUTES_BEFORE_INCOMING_ARRIVES);
				procedures.add(new AttackVillage(arrival, RAUSSTELL_COORDS));
				procedures.add(new SendResources(arrival, RAUSSTELL_COORDS));
			}
		}
		
		return procedures;
	}

}
