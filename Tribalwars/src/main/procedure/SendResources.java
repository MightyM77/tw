package main.procedure;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import config.TwConfiguration;
import tool.Market;

public class SendResources extends Procedure {

	private final Market market;
	private final Point destinationCoords;
	
	public SendResources(TwConfiguration pConfig, Market pMarket, Calendar pActivationTime, Point pDestinationCoords) {
		super(pConfig, pActivationTime.getTimeInMillis());
		this.market = pMarket;
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
