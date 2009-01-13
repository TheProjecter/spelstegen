package spelstegen.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spelstegen.client.entities.Match;
import spelstegen.client.entities.MatchDrawException;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Score;
import spelstegen.client.entities.Set;

/**
 * This class handles the calculation of points.
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */
public class LadderCalculator {
	
	private static final float ONE_SET_PERCENTAGE = 0.05f;
	private static final float THREE_SET_PERCENTAGE = 0.10f;
	private static final float FIVE_SET_PERCENTAGE = 0.15f;
	
	/**
	 * Calculates scores for players in matches.
	 * 
	 * @param matches a list of matches sorted by date
	 * @return A map with score history for all players that participated in the matches
	 * @throws MatchDrawException 
	 */
	public static Map<Player, List<Score>> calculateScore(List<Match> matches) throws MatchDrawException {
		Map<Player, List<Score>> playerScoreHistory = new HashMap<Player, List<Score>>();
		for (Match match : matches) {
			Player winner = getWinner(match);
			Player looser;
			float percentage = getPercentage(match);
			// Check which player that is the winner.
			if ( winner.equals(match.getPlayer1()) ) {
				looser = match.getPlayer2();
			} else {
				looser = match.getPlayer1();
			}
			int points = (int)(percentage * (float)looser.getPoints());
			winner.setPoints(winner.getPoints() + points);
			// The looser of the game only looses points if he has more or equal points to start with.
			if (winner.getPoints() < looser.getPoints()) {
				looser.setPoints( looser.getPoints() - points);
			}
			addPlayerScore(playerScoreHistory, match.getPlayer1(), match.getDate());
			addPlayerScore(playerScoreHistory, match.getPlayer2(), match.getDate());
		}
		return playerScoreHistory;
	}
	
	/**
	 * Determines the percentage to use when calculating how many percent of 
	 * the score the winner should get from the loser.
	 * 
	 * @param match the match
	 * @throws MatchDrawException when the match ended in draw which makes it impossible
	 * to determine how many sets are played, thus impossible to determine percentage
	 */
	private static float getPercentage(Match match) throws MatchDrawException {
		
		// If the match sport is a set of sports, like Racketlon, the sets
		// will consist of one set for each sport.
		if (!match.getSport().getChildSports().isEmpty()) {
			// ONE_SET_PERCENTAGE should be used since a set in parent sports should
			// be seen as the combination of one set in each of the child sports.
			return ONE_SET_PERCENTAGE;
		}
		// If the match is about a single sport
		else {
			switch (getWinningSets(match)) {
			case 3:
				return FIVE_SET_PERCENTAGE;
			case 2:
				return THREE_SET_PERCENTAGE;
			default:
				return ONE_SET_PERCENTAGE;
			}
		}
	}
	
	/**
	 * Returns the number of sets won by the winning player
	 * @param match the match
	 */
	private static int getWinningSets(Match match) throws MatchDrawException {
		int setsOneByPlayer1 = 0;
		int setsOneByPlayer2 = 0;
		for (Set set : match.getSets()) {
			if (set.getPlayer1Score() > set.getPlayer2Score()) {
				setsOneByPlayer1++;
			} else {
				setsOneByPlayer2++;
			}
		}
		if (setsOneByPlayer1 > setsOneByPlayer2) {
			return setsOneByPlayer1;
		} else if (setsOneByPlayer2 > setsOneByPlayer1) {
			return setsOneByPlayer2;
		} else {
			throw new MatchDrawException("Match '"+match.getId()+"' ended in a draw.");
		}
	}
	
	/**
	 * Determines the winner of a match.
	 * 
	 * @param match the match
	 * @return the player who won the match
	 */
	public static Player getWinner(Match match) throws MatchDrawException {
		
		// If the match sport is a set of sports, like Racketlon, the winner
		// is the one with highest total points when the points in each set
		// are added.
		if (!match.getSport().getChildSports().isEmpty()) {
			int player1Ponts = 0;
			int player2Ponts = 0;
			for (Set set : match.getSets()) {
				player1Ponts += set.getPlayer1Score();
				player2Ponts += set.getPlayer2Score();
			}
			if (player1Ponts > player2Ponts) {
				return match.getPlayer1();
			} else if (player2Ponts > player1Ponts) {
				return match.getPlayer2();
			} else {
				throw new MatchDrawException("Match '"+match.getId()+"' ended in a draw.");
			}
		}
		// If the match is about a single sport
		else {
			int setsOneByPlayer1 = 0;
			int setsOneByPlayer2 = 0;
			for (Set set : match.getSets()) {
				if (set.getPlayer1Score() > set.getPlayer2Score()) {
					setsOneByPlayer1++;
				} else if (set.getPlayer2Score() > set.getPlayer1Score()) {
					setsOneByPlayer2++;
				} else {
					throw new MatchDrawException("Set '"+set.getId()+"' in match '"+match.getId()+"' ended in a draw.");
				}
			}
			if (setsOneByPlayer1 > setsOneByPlayer2) {
				return match.getPlayer1();
			} else if (setsOneByPlayer2 > setsOneByPlayer1) {
				return match.getPlayer2();
			} else {
				throw new MatchDrawException("Match '"+match.getId()+"' ended in a draw.");
			}
		}
	}
	
	/**
	 * Gets a string with set and match results 
	 * @param match the match
	 */
	public static String getResultsString(Match match) {
		StringBuilder sb = new StringBuilder();

		if (!match.getSport().getChildSports().isEmpty()) {
			int player1Ponts = 0;
			int player2Ponts = 0;
			int i = 0;
			sb.append(" (");
			for (Set set : match.getSets()) {
				sb.append(set.getPlayer1Score());
				sb.append("-");
				sb.append(set.getPlayer2Score());
				if (i < (match.getSets().size() - 1)) {
					sb.append(",");
				}
				i++;
				player1Ponts += set.getPlayer1Score();
				player2Ponts += set.getPlayer2Score();
			}
			sb.append(")");
			return player1Ponts + "-" + player2Ponts + sb.toString();
		}
		// If the match is about a single sport
		else {
			int setsOneByPlayer1 = 0;
			int setsOneByPlayer2 = 0;
			int i = 0;
			sb.append(" (");
			for (Set set : match.getSets()) {
				sb.append(set.getPlayer1Score());
				sb.append("-");
				sb.append(set.getPlayer2Score());
				if (i < (match.getSets().size() - 1)) {
					sb.append(",");
				}
				i++;
				if (set.getPlayer1Score() > set.getPlayer2Score()) {
					setsOneByPlayer1++;
				} else if (set.getPlayer2Score() > set.getPlayer1Score()) {
					setsOneByPlayer2++;
				}
			}
			sb.append(")");
			return setsOneByPlayer1 + "-" + setsOneByPlayer2 + sb.toString();
		}
	}
	
	private static void addPlayerScore(Map<Player, List<Score>> playerScoreHistory, Player player, Date date) {
		List<Score> scoreHistory = playerScoreHistory.get(player);
		if (scoreHistory == null) {
			scoreHistory = new ArrayList<Score>();
		}
		scoreHistory.add(new Score(date, player.getPoints()));
		playerScoreHistory.put(player, scoreHistory);
	}
}
