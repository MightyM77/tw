package main.procedure.farming;

import java.awt.Point;
import java.util.Arrays;

import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmEntry;
import tool.farmassistant.Farmassistant;
import utile.ReportStatus;
import utile.Resource;
import config.TwConfiguration;

public class FarmEntryValidator {
	private final String name;
	private final Farmassistant fa;
	private final FarmButton buttonToClick;
	private final ReportStatus[] onlyThoseReportStatus;
	private final boolean onlyFullHaul;
	private final boolean notGettingAttacked;
	private final boolean validOnSecondRun;
	private final Point[] onlyThoseVillages;
	private final Point[] notThoseVillages;
	private final Resource moreResourcesThan;
	private final Resource lessResourcesThan;
	private final int wallBiggerThan;
	private final int wallLesserThan;
	private final double distanceBiggerThan;
	private final double distanceLesserThan;

	public static class Builder {
		// Required parameters
		private final FarmButton buttonToClick;
		private final Farmassistant fa;
		// Optional parameters - initialized to defaul values
		private String name = "";
		private ReportStatus[] onlyThoseReportStatus = new ReportStatus[0];
		private boolean onlyFullHaul = false;
		private boolean notGettingAttacked = false;
		private boolean validOnSecondRun = false;
		private Point[] onlyThoseVillages = new Point[0];
		private Point[] notThoseVillages = new Point[0];
		private Resource moreResourcesThan = new Resource(-1, -1, -1);
		private Resource lessResourcesThan = new Resource(1000000, 1000000, 1000000);
		private int wallBiggerThan = -1;
		private int wallLesserThan = 21;
		private double distanceBiggerThan = 0;
		private double distanceLesserThan = 1000000000;

		public Builder(Farmassistant pFa, FarmButton pButtonToClick) {
			this.fa = pFa;
			this.buttonToClick = pButtonToClick;
		}

		public Builder name(String pName) {
			name = pName;
			return this;
		}

		public Builder onlyThoseReportStatus(ReportStatus... pOnlyThoseReportStatus) {
			onlyThoseReportStatus = pOnlyThoseReportStatus;
			return this;
		}

		public Builder onlyFullHaul() {
			onlyFullHaul = true;
			return this;
		}

		public Builder notGettingAttacked() {
			notGettingAttacked = true;
			return this;
		}

		public Builder validOnSecondRun() {
			validOnSecondRun = true;
			return this;
		}

		public Builder onlyThoseVillages(Point... pOnlyThoseVillages) {
			onlyThoseVillages = pOnlyThoseVillages;
			return this;
		}

		public Builder notThoseVillages(Point... pNotThoseVillages) {
			notThoseVillages = pNotThoseVillages;
			return this;
		}

		public Builder moreResourcesThan(Resource pMoreResourcesThan) {
			moreResourcesThan = pMoreResourcesThan;
			return this;
		}

		public Builder lessResourcesThan(Resource pLessResourcesThan) {
			lessResourcesThan = pLessResourcesThan;
			return this;
		}

		public Builder biggerWallThan(int pBiggerWallThan) {
			wallBiggerThan = pBiggerWallThan;
			return this;
		}

		public Builder lessWallThan(int pLessWallThan) {
			wallLesserThan = pLessWallThan;
			return this;
		}

		public Builder biggerDistanceThan(double pBiggerDistanceThan) {
			distanceBiggerThan = pBiggerDistanceThan;
			return this;
		}

		public Builder lessDistanceThan(double pLessDistanceThan) {
			distanceLesserThan = pLessDistanceThan;
			return this;
		}

		public FarmEntryValidator build() {
			return new FarmEntryValidator(this);
		}
	}

	private FarmEntryValidator(Builder builder) {
		name = builder.name;
		this.fa = builder.fa;
		buttonToClick = builder.buttonToClick;
		onlyThoseReportStatus = builder.onlyThoseReportStatus;
		onlyFullHaul = builder.onlyFullHaul;
		notGettingAttacked = builder.notGettingAttacked;
		validOnSecondRun = builder.validOnSecondRun;
		onlyThoseVillages = builder.onlyThoseVillages;
		notThoseVillages = builder.notThoseVillages;
		moreResourcesThan = builder.moreResourcesThan;
		lessResourcesThan = builder.lessResourcesThan;
		wallBiggerThan = builder.wallBiggerThan;
		wallLesserThan = builder.wallLesserThan;
		distanceBiggerThan = builder.distanceBiggerThan;
		distanceLesserThan = builder.distanceLesserThan;
	}

	public boolean isValid(FarmEntry fe, int farmDurchgang) {
//		TwConfiguration.LOGGER.info("Starte Farmvalidation '{}'", name);
		
		boolean valid = true;
		String invalidText = "";
		
		if (!fe.isFarmButtonEnabled(buttonToClick)) {
			invalidText = "da " + buttonToClick.toString() + "-Farmbutton disabled ist";
			return false;
		}
		if (notGettingAttacked && fe.isGettingAttacked()) {
			invalidText = "da Barbarendorf bereits angegriffen wird";
			return false;
		}
		if (fe.getDistance() <= distanceBiggerThan) {
			invalidText = "distanz ist zu klein";
			return false;
		}
		if (fe.getDistance() >= distanceLesserThan) {
			invalidText = "distanz ist zu gross";
			return false;
		}
		if (onlyThoseReportStatus.length > 0 && !Arrays.asList(onlyThoseReportStatus).contains(fe.getOrFindReportStatus())) {
			invalidText = "wegen falschem ReportStatus";
			return false;
		}
		if (validOnSecondRun && farmDurchgang < 2) {
			invalidText = "da erst ab 2. Farmdurchgang valid";
			return false;
		}
		if (onlyFullHaul && !fe.isMaxLoot()) {
			invalidText = "da nicht MaxLoot";
			return false;
		}
		if (fe.getWallLevel() < wallBiggerThan) {
			invalidText = "wall ist zu klein";
			return false;
		}
		if (fe.getWallLevel() >= wallLesserThan) {
			invalidText = "wall ist zu gross";
			return false;
		}
		if (onlyThoseVillages.length > 0 && !Arrays.asList(onlyThoseVillages).contains(fe.getOrFindDestCoords())) {
			invalidText =  "da Koordinaten nicht auf white list sind";
			return false;
		}
		if (notThoseVillages.length > 0 && Arrays.asList(notThoseVillages).contains(fe.getOrFindDestCoords())) {
			invalidText = "da Koordinaten auf blacklist sind";
			return false;
		}
		if (moreResourcesThan.biggerThan(fe.getResources())) {
			invalidText = "kleiner als erwartet sind";
			return false;
		}
		if (!lessResourcesThan.biggerThan(fe.getResources())) {
			invalidText = "grösser als erwartet sind";
			return false;
		}
		
//		if (!valid) {
//			TwConfiguration.LOGGER.info("Farmeintrag ({}-Button) nicht valid, {}", buttonToClick.toString(), invalidText);
//		}
		
		return valid;
	}

	public FarmButton getButtonToClick() {
		return buttonToClick;
	}
	
	public boolean isValidOnSecondRund() {
		return this.validOnSecondRun;
	}
}