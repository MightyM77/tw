package utile;

import java.awt.Point;

public final class GameHelper {
	
	private static final GameHelper INSTANCE = new GameHelper();

	private GameHelper() {
		// singleton
	}
	
	public static GameHelper getInstance() {
		return GameHelper.INSTANCE;
	}
	
	public int getWalkingDurationSeconds(Point coord1, Point coord2, Troop troop) {
		double x = coord1.getX() - coord2.getX();
		double y = coord1.getY() - coord2.getY();
		
		double fields = Math.sqrt((x*x) + (y*y));
		return (int) Math.round(troop.getWalkingDurationSeconds()*fields);
	}
	
	public Point getCoordFromString(String string) {
		String[] splitString = string.split("\\|");
		int x = Integer.valueOf(splitString[0].substring(splitString[0].lastIndexOf('(') + 1));
		int y = Integer.valueOf(splitString[1].substring(0,3));
		return new Point(x, y);
	}
}
