package com.appspot.spelstegen.client.services;

import java.util.List;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Main server interface
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */
@RemoteServiceRelativePath("spelstegenService")
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
	public void savePlayer(Player player);
	
	/**
	 * Get the list of all players.
	 * @return
	 */
	public List<Player> getPlayers();
	
	/**
	 * Save a match
	 * @param match match to save
	 * @param playerToSave the player that wants to save the match
	 */
	public void saveMatch(Match match, Player playerToSave);
	
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
	 * @param leagueId
	 * @return
	 */
	public League getLeague(Long leagueId);
	
	/**
	 * Get the version string from the server.
	 * @return the version string.
	 */
	public String getVersionString();
	
}
