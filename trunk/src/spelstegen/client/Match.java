package spelstegen.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Match class
 * 
 * @author Henrik Segesten
 *
 */
public class Match implements Serializable {

	private int matchid;
	private Date date;
	private String player1Email;
	private String player2Email;
	private List<Integer[]> sets;
	private int setsWonByPlayer1;
	private int setsWonByPlayer2;
	private String season;
	
	public Match() {
		super();
		sets = new ArrayList<Integer[]>();
	}
	
	public Match(int matchId, String season, Date date, String player1Email, String player2Email) {
		this();
		this.matchid = matchId;
		this.season = season;
		this.date = date;
		this.player1Email = player1Email;
		this.player2Email = player2Email;
	}
	
	public Match(String season, Date date, String player1Email, String player2Email) {
		this(-1, season, date, player1Email, player2Email);
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
	
	public String getSeason() {
		return season;
	}
	
	public void setSeason(String season) {
		this.season = season;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getMatchId() {
		return matchid;
	}
	
	public String[] getPlayers() {
		return new String[] {player1Email, player2Email};
	}
	
	public static class MatchDoneException extends Exception {
		
		public MatchDoneException() {
			super();
		}
		
		public MatchDoneException(String message) {
			super(message);
		}
	}
	
	public String getWinner() {
		int setsPlayed = sets.size();
		if (setsPlayed == 1 || setsPlayed == 3 || setsPlayed == 5) {
			if (setsWonByPlayer1 > setsWonByPlayer2) {
				return player1Email;
			} else {
				return player2Email;
			}
		}
		return null;
	}
	
	public String getLoser() {
		int setsPlayed = sets.size();
		if (setsPlayed == 1 || setsPlayed == 3 || setsPlayed == 5) {
			if (setsWonByPlayer1 > setsWonByPlayer2) {
				return player2Email;
			} else {
				return player1Email;
			}
		}
		return null;
	}

	public int getNumberOfSets() {
		return sets.size();
	}
	
	public String getScores(boolean insertSpace) {
		StringBuilder sb = new StringBuilder();
		for (Integer[] set : sets) {
			if (sb.length() != 0) {
				sb.append(",");
				if (insertSpace) {
					sb.append(" ");
				}
			}
			sb.append(set[0] + "-" + set[1]);
		}
		return sb.toString();
	}
}
