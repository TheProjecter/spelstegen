package spelstegen.client.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a league
 * 
 * @author Per Mattsson
 */
public class League implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private List<Player> players;
	private List<Sport> sports;
	private List<Season> seasons;
	
	public League() {
		this(null);
	}
	
	public League(String name) {
		this.name = name;
		players = new ArrayList<Player>();
		sports = new ArrayList<Sport>();
		seasons = new ArrayList<Season>();
		seasons.add( Season.createInitalSeason() );
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

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Sport> getSports() {
		return sports;
	}

	public void setSports(List<Sport> sports) {
		this.sports = sports;
	}

	public List<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		League other = (League) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
