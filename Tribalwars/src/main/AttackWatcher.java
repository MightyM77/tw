package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tool.Overview;

public class AttackWatcher extends Procedure {

	public AttackWatcher(Calendar pActivationTime) {
		super(pActivationTime);
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		Overview ov = Overview.getInstance();
		ov.goToSite();
//		WebElement attacks =  
		
		return null;
	}

}
