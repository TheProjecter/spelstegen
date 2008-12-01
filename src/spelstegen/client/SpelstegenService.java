package spelstegen.client;

import java.util.List;

import spelstegen.client.entities.League;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.Player;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Main server interface
 * @author Henrik Segesten
 */
public interface SpelstegenService extends RemoteService {

	/**
	 * Checks the password and if correct returns the player object.
	 * @param email
	 * @param password
	 * @return
	 */
	public Player logIn(String email, String password);
	
	/**
	 * Add a new player
	 * @param player
	 * @return
	 */
	public boolean addPlayer(Player player);
	
	/**
	 * Save new details of a player.
	 * @param player
	 * @return
	 */
	public boolean updatePlayer(Player player);
	
	/**
	 * Get the list of all players.
	 * @return
	 */
	public List<Player> getPlayers();
	
	/**
	 * Add a new match
	 * @param match
	 * @param leagueId the league to which the match belongs
	 */
	public void addMatch(Match match, int leagueId);
	
	/**
	 * Get a list of all matches for a specific league.
	 * @param league The league to get matches for, if null all matches will be returned.
	 * @return
	 */
	public List<Match> getMatches(League league);
	
	/**
	 * Gets all leagues that a player is a member of or all leagues if player is null
	 * @param player the player
	 */
	public List<League> getLeagues(Player player);
	
}
