package com.appspot.spelstegen.server.persistence;

import java.util.Collection;
import java.util.List;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Sport;

/**
 * Interface to persistence manager. The persistence manager handles
 * storing/fetching data from/to a non-volatile storage.
 * 
 * @author Per Mattsson
 */
public interface PersistenceManager {
	
	/**
	 * Stores a league
	 * 
	 * @param league
	 *            the league to store
	 */
	public void storeLeague(League league);

	/**
	 * Fetch a league with a specified id.
	 * 
	 * @param leagueId
	 * @return league with specified id if it exists; otherwise null
	 */
	public League getLeague(Long leagueId);

	/**
	 * Add a new player
	 * 
	 * @param player
	 */
	public void storePlayer(Player player);

	/**
	 * Fetches a player based on id.
	 * 
	 * @param id
	 *            player id
	 * @return the player if available; otherwise null
	 */
	public Player getPlayer(Integer id);

	/**
	 * Fetches a player based on email.
	 * 
	 * @param email
	 *            the players email
	 * @return the player if available; otherwise null
	 */
	public Player getPlayer(String email);
	
	/**
	 * Fetches all players that belongs to a specific league
	 * 
	 * @param leagueId
	 *            id of the league to get players, or null to get players from all
	 *            leagues.
	 * @return a list of players
	 */
	public List<Player> getPlayers(Long leagueId);
	
	/**
	 * Fetches all players
	 * 
	 * @return a collection of players
	 */
	public Collection<Player> getAllPlayers();

	/**
	 * Stores a match
	 * 
	 * @param match
	 *            the match to store
	 */
	public void storeMatch(Match match);

	/**
	 * Fetch all matches in a league.
	 * 
	 * @param leagueId
	 *            id of league
	 * @return a list with all matches in a league
	 */
	public List<Match> getMatches(Long leagueId);

	/**
	 * Fetches all leagues that a specific player is a member of
	 * 
	 * @param player
	 *            the player
	 * @return a list with all leagues that the player is a member of
	 */
	public List<League> getLeagues(Player player);
	
	/**
	 * Fetches all leagues
	 * 
	 * @return a collection with all leagues
	 */
	public Collection<League> getAllLeagues();

	/**
	 * Fetch all available sports.
	 * 
	 * @return a list with all available sports
	 */
	public List<Sport> getSports();
	
	

	

}
