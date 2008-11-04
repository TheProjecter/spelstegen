package spelstegen.client;

import java.util.List;

/**
 * This class handles the calculation of points.
 * @author Henrik Segesten
 */
public class LadderCalculator {
	
	private static final float ONE_SET_PERCENTAGE = 0.05f;
	private static final float THREE_SET_PERCENTAGE = 0.10f;
	private static final float FIVE_SET_PERCENTAGE = 0.15f;
	
	public LadderCalculator() {
		super();
	}
	
	public void reset() {
		// Init all players to have 1000 points.
		for (Player player : MainApplication.players.values()) {
			player.changePoints(1000 - player.getPoints());
		}
	}
	
	public void addMatches(List<Match> matches) {
		for (Match match : matches) {
			addMatch(match);
		}
	}
	
	public void addMatch(Match match) {
		float percentage;
		switch (match.getNumberOfSets()) {
		case 1:
			percentage = ONE_SET_PERCENTAGE;
			break;
		case 3:
			percentage = THREE_SET_PERCENTAGE;
			break;
		case 5:
			percentage = FIVE_SET_PERCENTAGE;
			break;
		default:
			// In this case the match is not finished. No points will be redistributed.
			percentage = 0f;
			return;
		}
		int pointsToTransfer = Math.round(MainApplication.getPlayer(match.getLoser()).getPoints() * percentage);
		MainApplication.getPlayer(match.getLoser()).changePoints(-pointsToTransfer);
		MainApplication.getPlayer(match.getWinner()).changePoints(pointsToTransfer);
	}

}
