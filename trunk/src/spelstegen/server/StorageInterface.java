package spelstegen.server;

import java.util.List;

import spelstegen.client.entities.League;
import spelstegen.client.entities.LeagueSummary;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.ScoreHistory;
import spelstegen.client.entities.Sport;

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
	 * Update player data
	 * @param player
	 * @return
	 */
	public boolean updatePlayer(Player player);
	
	/**
	 * Get the list of all players.
	 * @param leagueId the league whose players to get, or -1 if for all leagues.
	 * @return
	 */
	public List<Player> getPlayers(int leagueId);
	
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
	 * @param leagueId the league to which the match belongs
	 */
	public void addMatch(Match match, int leagueId);
	
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

	/**
	 * Gets score history for a player.
	 */
	public List<ScoreHistory> getScoreHistory(Player player);
	
	/**
	 * Add a player to a league.
	 * @param playerid The player to add
	 * @param leagueid to this league
	 */
	public void addPlayerToLeague(int leagueid, int playerid);
	
	/**
	 * Get a summary of all leagues
	 * @return
	 */
	public List<LeagueSummary> getLeagueSummaries();
	
	/**
	 * Get a single league based on it's id.
	 * @param id
	 * @return
	 */
	public League getLeague(int id);
	
	/**
	 * Create a new league
	 * @param league
	 */
	public void addLeague(League league);

	/**
	 * Check if the player is part of a league
	 * @param playerId
	 * @param leagueId
	 * @return
	 */
	public boolean isPlayerInLeague(int playerId, int leagueId);
	
	/**
	 * Check if the player is a leagueMatchAdmin for the league
	 * @param leagueId
	 * @param playerId
	 * @return true if the player is leagueMatchAdmin, false otherwise
	 */
	public boolean isPlayerLeagueMatchAdmin(int playerId, int leagueId);
	
	/**
	 * Check if the player is a leagueAdmin for the league
	 * @param playerId
	 * @param leagueId
	 * @return true if the player is leagueAdmin, false otherwise
	 */
	public boolean isPlayerLeagueAdmin(int playerId, int leagueId);
	
	/**
	 * Adds admin roles to a player for a certain league.
	 * @param leagueId the league to add admin roles for
	 * @param playerId the player to add admin roles for
	 * @param leagueAdmin true if the player should be leagueAdmin
	 * @param matchAdmin true if the player should be matchAdmin
	 */
	public void addLeagueAdminRoles(int playerId, int leagueId, boolean leagueAdmin, boolean matchAdmin);
	
	/**
	 * Get all league admins for a league.
	 * @param leagueId
	 * @return
	 */
	public List<Integer> getAllLeagueAdmins(int leagueId);
	
	/**
	 * Get all match admins for a league.
	 * @param leagueId
	 * @return
	 */
	public List<Integer> getAllMatchAdmins(int leagueId);
}
