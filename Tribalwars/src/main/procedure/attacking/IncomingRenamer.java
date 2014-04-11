package main.procedure.attacking;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tool.overview_villages.incoming.IncomingAttack;
import utile.Troop;
import config.TwConfiguration;
import main.procedure.IncomingAttacksWatcher;
import main.procedure.Procedure;

public class IncomingRenamer extends Procedure {

	private final IncomingAttack incomingAttack;
	
	public IncomingRenamer(TwConfiguration pConfig, IncomingAttack pIncomingAttack, Calendar pActivationTime) {
		super(pConfig, pActivationTime.getTimeInMillis());
		this.incomingAttack = pIncomingAttack;
	}

	@Override
	public List<Procedure> doAction() throws ParseException {
		TwConfiguration.LOGGER.info("Starte Incoming-Attacks-Watcher-Procedur");
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		incomingAttack.goToSite();
		
		int incomingsCount = incomingAttack.getIncomingsCount();
		if (incomingsCount > 0) {
			for (int i = 0; i < incomingsCount; i++) {
				if (incomingAttack.getIncomingName(i).contains("Attack")) {
					TwConfiguration.LOGGER.info("Neuer Angriff erkannt. Angreifer: {} Ankunftszeit: {}", incomingAttack.getAttackerName(i), incomingAttack.getArrivingTime(i).getTime());
					Calendar arrival = incomingAttack.getArrivingTime(i);
					Point originCoords = incomingAttack.getOriginCoord(i);
					Point destCoords = incomingAttack.getDestinationCoord(i);
					
					String incomingName = "";
					
					for (Troop troop : Troop.values()) {
						int walkingDurationSeconds = troop.getWalkingDurationSeconds(originCoords, destCoords);
						Calendar theoreticalArrival = Calendar.getInstance();
						theoreticalArrival.add(Calendar.SECOND, walkingDurationSeconds);
						if (arrival.compareTo(theoreticalArrival) < 0) {
							incomingName += troop.toString() + " ";
						}
					}
					
					incomingAttack.ranameIncoming(i, incomingName);
				}
			}
		}
		
		Calendar newActivationTime = Calendar.getInstance();
		newActivationTime.add(Calendar.MINUTE, 5);
		procedures.add(new IncomingRenamer(getTwConfig(), incomingAttack, newActivationTime));
		
		return procedures;
	}

}
