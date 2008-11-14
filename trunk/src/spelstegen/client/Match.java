package spelstegen.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Match class
 * 
 * @author Henrik Segesten
 *
 */
public class Match implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Date date;
	private Sport sport;
	private Player player1;
	private Player player2;
	private List<Set> sets;
	
	public Match() {}

	public Match(Date date, Sport sport, Player player1,
			Player player2, List<Set> sets) {
		this(0, date, sport, player1, player2, sets);
	}

	public Match(int id, Date date, Sport sport, Player player1,
			Player player2, List<Set> sets) {
		this.id = id;
		this.date = date;
		this.sport = sport;
		this.player1 = player1;
		this.player2 = player2;
		this.sets = sets;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public List<Set> getSets() {
		return sets;
	}

	public void setSets(List<Set> sets) {
		this.sets = sets;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String toString() {
		return "Match " + id + ", datum: " + date.toString() + ", sport: " + sport.getName() + ", spelare1: " + player1
		+ ", spelare2: " + player2 + ", sets: " + LadderCalculator.getResultsString(this); 
	}
}
