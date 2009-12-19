package com.appspot.spelstegen.client.services;

import java.util.List;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous version of the server interface.
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */
public interface SpelstegenServiceAsync {

	public void logIn(String email, String password, AsyncCallback<Player> callback);
	
	public void savePlayer(Player player, AsyncCallback<Void> callback);
	
	public void getPlayers(AsyncCallback<List<Player>> callback);
	
	public void saveMatch(Match match, Player playerToSave, AsyncCallback<Void> callback);
	
	public void getMatches(League league, AsyncCallback<List<Match>> callback);
	
	public void getLeagues(Player player, AsyncCallback<List<League>> callback);
	
	public void getLeagueSummaries(AsyncCallback<List<LeagueSummary>> callback);
	
	public void getLeague(Integer id, AsyncCallback<League> callback);
		
	public void getVersionString(AsyncCallback<String> callback);
	
}
