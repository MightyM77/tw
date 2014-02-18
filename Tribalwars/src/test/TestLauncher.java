package test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tool.farmassistant.FarmTemplate;
import utile.ReportStatus;
import main.procedure.Procedure;
import main.procedure.farming.FarmassistantFarming;

public class TestLauncher {

	public static void main(String[] args) {
		List<Procedure> procedures = new ArrayList<Procedure>();
		FarmTemplate[] farmTemplatesToClick = new FarmTemplate[] { FarmTemplate.A, FarmTemplate.B };
		ReportStatus[] onlyThoseReportStatus = new ReportStatus[] { ReportStatus.NO_LOSSES };
		for (int i = 0; i < 100000; i++) {
			procedures.add(new FarmassistantFarming(Calendar.getInstance(), onlyThoseReportStatus, farmTemplatesToClick));
			System.out.println("Memory used: " + Runtime.getRuntime().totalMemory());
			System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}