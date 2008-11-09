package spelstegen.server;

import java.util.List;

import spelstegen.client.League;
import spelstegen.client.Match;
import spelstegen.client.Player;
import spelstegen.client.Sport;

/**
 * This is the main interface to data storage.
 * @author Henrik Segesten
 *
 */
public interface StorageInterface {

	/**
	 * Add a new player
	 * @param player
	 * @return
	 */
	public boolean addPlayer(Player player);
	
	/**
	 * Get the list of all players.
	 * @return
	 */
	public List<Player> getPlayers();
	
	/**
	 * Gets a single player based on id.
	 * @param id
	 * @return
	 */
	public Player getPlayer(int id);
	
	/**
	 * Gets a single player based on email.
	 * @param id
	 * @return
	 */
	public Player getPlayer(String email);
	
	/**
	 * Add a new match
	 * @param match
	 * @param league the league to which the match belongs
	 */
	public void addMatch(Match match, League league);
	
	/**
	 * Gets all matches in a league.
	 * @param league
	 */
	public List<Match> getMatches(League league);
	
	/**
	 * Gets all leagues that a player is a member of
	 * @param player the player
	 */
	public List<League> getLeagues(Player player);
	
	/**
	 * Gets all available sports.
	 */
	public List<Sport> getSports();
}
