package spelstegen.client;

import java.io.Serializable;

/**
 * Class representing a player
 * 
 * @author Henrik Segesten
 */
public class Player implements Serializable, Comparable<Player> {
	
	private String playerId;
	private String playerName;
	private int points = 0;
	
	public Player() {
		super();
	}
	
	public Player(String playerId, String playerName) {
		this.playerId = playerId;
		this.playerName = playerName;
	}
	
	public Player(String playerName) {
		this(null, playerName);
	}
	
	public String getPlayerId() {
		return playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getPoints() {
		return points;
	}
	public void changePoints(int amount) {
		points += amount;
	}

	public int compareTo(Player o) {
		if (points == o.getPoints()) {
			return 0;
		} else if (points < o.getPoints()) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
