package spelstegen.client;

import java.util.List;

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
	 * Get the list of all players.
	 * @return
	 */
	public List<Player> getPlayers();
	
	/**
	 * Add a new match
	 * @param match
	 */
	public void addMatch(Match match);
	
	/**
	 * Get a list of all matches for a specific season.
	 * @param season The season to get matches for, if null all matches will be returned.
	 * @return
	 */
	public List<Match> getMatches(String season);
	
}
