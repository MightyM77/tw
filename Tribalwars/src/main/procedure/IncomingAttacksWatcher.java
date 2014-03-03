package main.procedure;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import main.procedure.attacking.AttackVillage;
import tool.Market;
import tool.Place;
import tool.overview_villages.incoming.IncomingAttack;
import config.TwConfiguration;

public class IncomingAttacksWatcher extends Procedure {

	private static final int MINUTES_BEFORE_INCOMING_ARRIVES = -3;
	private static final Point RAUSSTELL_COORDS = new Point(493,498);
	private final IncomingAttack incomingAttack;
	private final Place place; 
	private final Market market;
	
	public IncomingAttacksWatcher(TwConfiguration pConfig, IncomingAttack pIncomingAttack, Place pPlace, Market pMarket, Calendar pActivationTime) {
		super(pConfig, pActivationTime);
		this.incomingAttack = pIncomingAttack;
		this.place = pPlace;
		this.market = pMarket;
	}

	@Override
	public List<Procedure> doAction() throws ParseException {
		TwConfiguration.LOGGER.info("Starte Incoming-Attacks-Watcher-Procedur");
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		incomingAttack.goToSite();
		
		int incomingsCount = incomingAttack.getIncomingsCount();
		if (incomingsCount > 0) {
			for (int i = 0; i < incomingsCount; i++) {
				Calendar arrival = incomingAttack.getArrivingTime(i);
				TwConfiguration.LOGGER.info("Angriff erkannt. Angreifer: {} Ankunftszeit: {}", incomingAttack.getAttackerName(i), incomingAttack.getArrivingTime(i).getTime());
				arrival.add(Calendar.MINUTE, MINUTES_BEFORE_INCOMING_ARRIVES);
				procedures.add(new AttackVillage(config(), place, arrival, RAUSSTELL_COORDS));
				procedures.add(new SendResources(config(), market, arrival, RAUSSTELL_COORDS));
			}
		}
		
		return procedures;
	}

}
