package spelstegen.client.entities;

import java.io.Serializable;

/**
 * This class is used to send quick summaries of a league.
 * @author Henrik Segesten
 */
public class LeagueSummary implements Serializable {

	private int id;
	private String name;
	private int numberOfPlayers;
	
	public LeagueSummary() {
		super();
	}

	public LeagueSummary(int id, String name, int numberOfPlayers) {
		this.id = id;
		this.name = name;
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
}
