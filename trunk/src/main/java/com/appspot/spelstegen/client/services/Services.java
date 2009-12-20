package com.appspot.spelstegen.client.services;

import java.util.List;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface to all services available to the client
 * 
 * @author Per Mattsson
 */
public interface Services {

	/**
	 * Registers a update listener.
	 */
	public void addLeagueUpdateListener(LeagueUpdateListener listener);
	
	public void getLeague(Long id);
	
	public void getLeagues(Player player, AsyncCallback<List<League>> callback);
	
	public void getLeagueSummaries(AsyncCallback<List<LeagueSummary>> callback);
	
	public void addLoginListener(LoginListener listener);
	
	public void logIn(String email, String password, AsyncCallback<Player> callback);
	
	public void logOut();
	
	public void savePlayer(Player player, League league, AsyncCallback<Void> callback);
	
	public void getPlayers(AsyncCallback<List<Player>> callback);
	
	public void saveMatch(Match match, Player playerToSave, League league, AsyncCallback<Void> callback);
	
	public void getVersionString(AsyncCallback<String> callback);
}
