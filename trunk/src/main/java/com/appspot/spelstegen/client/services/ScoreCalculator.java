package com.appspot.spelstegen.client.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.MatchDrawException;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Score;
import com.appspot.spelstegen.client.entities.Set;

/**
 * This class handles the calculation of scores.
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */
public class ScoreCalculator {
	
	private static final int INITIAL_SCORE = 1000;
	private static final float ONE_SET_PERCENTAGE = 0.05f;
	private static final float THREE_SET_PERCENTAGE = 0.10f;
	private static final float FIVE_SET_PERCENTAGE = 0.15f;

	public static class PlayerScoreMapComparator implements Comparator<Entry<Player, Integer>> {

		@Override
		public int compare(Entry<Player, Integer> o1, Entry<Player, Integer> o2) {
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			int result = o2.getValue().compareTo(o1.getValue());
			if (result == 0) {
				result = o1.getKey().getPlayerName().compareTo(o2.getKey().getPlayerName());
			}
			return result;
		}
		
	}
	
	/**
	 * Calculates current score for a list of players
	 * 
	 * @param players
	 *            all players
	 * @param matches
	 *            matches that players have participated in
	 * @return A set that contains current scores for each player in the list
	 * @throws MatchDrawException
	 *             if error
	 */
	public static java.util.Set<Entry<Player, Integer>> calculateCurrentScoreForPlayers(List<Player> players, List<Match> matches) throws MatchDrawException {
		java.util.Set<Entry<Player, Integer>> currentScoresSet = new TreeSet<Entry<Player, Integer>>(new PlayerScoreMapComparator());
		Map<Player, Integer> currentScores = new HashMap<Player, Integer>();
		Map<Player, List<Score>> scoreHistoryForPlayers = calculateScore(matches);
		for (Player player : players) {
			List<Score> playerScoreHistory = scoreHistoryForPlayers.get(player);
			if (playerScoreHistory != null) {
				currentScores.put(player, playerScoreHistory.get(playerScoreHistory.size() - 1).getScore());
			} else {
				currentScores.put(player, INITIAL_SCORE);
			}
		}
		currentScoresSet.addAll( currentScores.entrySet() );
		return currentScoresSet;
	}

	
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
			int currentScoreForLooser = getCurrentScore(playerScoreHistory.get(looser));
			int score = (int)(percentage * (float)currentScoreForLooser);
			int currentScoreForWinner = addPlayerScore(playerScoreHistory, winner, match.getDate(), score);
			
			// The looser of the game only looses points if he has more or equal points to start with.
			if (currentScoreForWinner < currentScoreForLooser) {
				addPlayerScore(playerScoreHistory, looser, match.getDate(), score * -1);
			}
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
		// is the one with highest total score when the score for each set
		// is added.
		if (!match.getSport().getChildSports().isEmpty()) {
			int player1Score = 0;
			int player2Score = 0;
			for (Set set : match.getSets()) {
				player1Score += set.getPlayer1Score();
				player2Score += set.getPlayer2Score();
			}
			if (player1Score > player2Score) {
				return match.getPlayer1();
			} else if (player2Score > player1Score) {
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
	
	private static int addPlayerScore(Map<Player, List<Score>> playerScoreHistory, Player player, Date date, int scoreToAdd) {
		List<Score> scoreHistory = playerScoreHistory.get(player);
		if (scoreHistory == null) {
			scoreHistory = new ArrayList<Score>();
			playerScoreHistory.put(player, scoreHistory);
		}
		int newScore = getCurrentScore(scoreHistory) + scoreToAdd;
		scoreHistory.add(new Score(date, newScore));
		
		return newScore;
	}
	
	private static int getCurrentScore(List<Score> scoreHistory) {
		int currentScore;
		if ((scoreHistory == null) || (scoreHistory.isEmpty())) {
			currentScore = INITIAL_SCORE;
		} else {
			currentScore = scoreHistory.get(scoreHistory.size() - 1).getScore();
		}
		return currentScore;
	}
}
