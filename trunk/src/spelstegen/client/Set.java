package spelstegen.client;

import java.io.Serializable;

/**
 * Class representing a set
 * 
 * @author Henrik Segesten
 */
public class Set implements Serializable {

	private int id;
	private int player1Score;
	private int player2Score;
	private int matchId;
	private int sportId;
	
	public Set() {
		super();
	}
	
	public Set(int id, int sportId, int player1Score, int player2Score, int matchId) {
		super();
		this.id = id;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
		this.matchId = matchId;
		this.sportId = sportId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlayer1Score() {
		return player1Score;
	}
	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}
	public int getPlayer2Score() {
		return player2Score;
	}
	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public int getSportId() {
		return sportId;
	}
	public void setSportId(int sportId) {
		this.sportId = sportId;
	}
	
}
