package spelstegen.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Match implements Serializable {

	private String matchid;
	private Date date;
	private Player player1;
	private Player player2;
	private List<Integer[]> sets;
	private int setsWonByPlayer1;
	private int setsWonByPlayer2;
	
	public Match() {
		super();
		sets = new ArrayList<Integer[]>();
	}
	
	public Match(String matchId, Date date, Player player1, Player player2) {
		this();
		this.matchid = matchId;
		this.date = date;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public Match(Date date, Player player1, Player player2) {
		this(null, date, player1, player2);
	}
	
	public void addSet(int score1, int score2) throws MatchDoneException {
		if (sets.size() <= 5) {
			sets.add(new Integer[] {score1, score2});
			if (score1 > score2) {
				setsWonByPlayer1++;
			} else if (score2 > score1) {
				setsWonByPlayer2++;
			}
		} else {
			throw new MatchDoneException("No more than 5 sets per match.");
		}
	}
	
	public void updateSet(int set, int score1, int score2) {
		if (sets.size() > set) {
			Integer[] oldScore = sets.get(set);
			if (oldScore[0] > oldScore[1]) {
				setsWonByPlayer1--;
			} else {
				setsWonByPlayer2--;
			}
			sets.get(set)[0] = score1;
			sets.get(set)[1] = score2;
			if (score1 > score2) {
				setsWonByPlayer1++;
			} else if (score2 > score1) {
				setsWonByPlayer2++;
			}
		}
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMatchId() {
		return matchid;
	}
	
	public Player[] getPlayers() {
		return new Player[] {player1, player2};
	}
	
	public static class MatchDoneException extends Exception {
		
		public MatchDoneException() {
			super();
		}
		
		public MatchDoneException(String message) {
			super(message);
		}
	}
	
	public Player getWinner() {
		int setsPlayed = sets.size();
		if (setsPlayed == 1 || setsPlayed == 3 || setsPlayed == 5) {
			if (setsWonByPlayer1 > setsWonByPlayer2) {
				return player1;
			} else {
				return player2;
			}
		}
		return null;
	}
	
	public Player getLoser() {
		int setsPlayed = sets.size();
		if (setsPlayed == 1 || setsPlayed == 3 || setsPlayed == 5) {
			if (setsWonByPlayer1 > setsWonByPlayer2) {
				return player2;
			} else {
				return player1;
			}
		}
		return null;
	}

	public int getNumberOfSets() {
		return sets.size();
	}
	
	public String getScores() {
		StringBuilder sb = new StringBuilder();
		for (Integer[] set : sets) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(set[0] + "-" + set[1]);
		}
		return sb.toString();
	}
}
