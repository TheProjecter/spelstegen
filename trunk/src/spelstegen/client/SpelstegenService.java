package spelstegen.client;

import java.util.List;
import java.util.Map;

import spelstegen.client.entities.League;
import spelstegen.client.entities.LeagueSummary;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Player.PlayerStatus;

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
	 * @param playerId the player which adds the match
	 */
	public boolean addMatch(Match match, int leagueId, int playerId);
	
	/**
	 * Add a player to a league
	 * @param leagueId the league to add the player to
	 * @param playerIdToAdd the id of the player to add
	 * @param playerIdAdder the id of the player which adds
	 */
	public void addPlayerToLeague(int leagueId, int playerIdToAdd, int playerIdAdder);
	
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
	
	/**
	 * Gets a summary of all leagues.
	 */
	public List<LeagueSummary> getLeagueSummaries();
	
	/**
	 * Get a single league based on it's id.
	 * @param id
	 * @return
	 */
	public League getLeague(int id);
	
	
	/**
	 * Gets a players status in a league.
	 * @param leagueId
	 * @param playerId
	 * @return
	 */
	public PlayerStatus getPlayerStatus(int leagueId, int playerId);
	
	/**
	 * Get the player status of all players in a league.
	 * @param leagueId
	 * @return
	 */
	public Map<Integer, PlayerStatus> getLeaguePlayersStatus(int leagueId);
	
	/**
	 * Adds this player as a league administrator
	 * @param leagueId the league the player should be admin of.
	 * @param playerToAddId the player to add.
	 * @param playerAddingId the player doing the add.
	 * @return true if success, false otherwise
	 */
	public boolean addLeageAdmin(int leagueId, int playerToAddId, int playerAddingId);
	
	/**
	 * Adds this player as a league match administrator
	 * @param leagueId the league the player should be match admin of.
	 * @param playerToAddId the player to add.
	 * @param playerAddingId the player doing the add.
	 * @return true if success, false otherwise
	 */
	public boolean addLeagueMatchAdmin(int leagueId, int playerToAddId, int playerAddingId);
	
	/**
	 * Get the version string from the server.
	 * @return the version string.
	 */
	public String getVersionString();
	
}
