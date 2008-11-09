package spelstegen.client;

import java.io.Serializable;

/**
 * Class representing a set
 * 
 * @author Henrik Segesten
 */
public class Set implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Sport sport;
	private int player1Score;
	private int player2Score;
	
	public Set() {}

	public Set(int id, Sport sport, int player1Score, int player2Score) {
		super();
		this.id = id;
		this.sport = sport;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
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
}
