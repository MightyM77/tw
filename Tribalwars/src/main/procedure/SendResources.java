package main.procedure;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tool.Market;

public class SendResources extends Procedure {

	private final Point destinationCoords;
	private final Market market = Market.getInstance();
	
	public SendResources(Calendar pActivationTime, Point pDestinationCoords) {
		super(pActivationTime);
		this.destinationCoords = pDestinationCoords;
	}

	@Override
	public List<Procedure> doAction() throws ParseException {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		market.goToSite();
		market.sendMax(destinationCoords);
		
		return procedures;
	}

}
