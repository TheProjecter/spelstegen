package com.appspot.spelstegen.client.services;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Class that manages server-side services and handles notifications to
 * listeners.
 * 
 * @author Per Mattsson
 */
public class ServiceManager implements Services {

	private static ServiceManager instance;
	private SpelstegenServiceAsync spelstegenService;

	private List<LeagueUpdateListener> leagueUpdateListeners = new ArrayList<LeagueUpdateListener>();
	private List<LoginListener> loginListeners = new ArrayList<LoginListener>();
	private List<MatchListUpdateListener> matchListUpdateListeners = new ArrayList<MatchListUpdateListener>();

	private ServiceManager() {
		spelstegenService = (SpelstegenServiceAsync) GWT
				.create(SpelstegenService.class);
	}

	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}

	@Override
	public void addLeagueUpdateListener(LeagueUpdateListener listener) {
		leagueUpdateListeners.add(listener);
	}

	private void notifyLeagueUpdated(League league) {
		for (LeagueUpdateListener listener : leagueUpdateListeners) {
			listener.leagueUpdated(league);
		}
	}

	@Override
	public void addLoginListener(LoginListener listener) {
		loginListeners.add(listener);
	}
	
	@Override
	public void logIn(final String email, final String password,
			final AsyncCallback<Player> listenerCallback) {
		
		final AsyncCallback<Player> callback = new AsyncCallback<Player>() {
			public void onFailure(Throwable e) {
				Log.error("Unexpected error when trying to login [e-mail:"+email+", password:"+password+"]: " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(Player player) {
				if (player != null) {
					Log.info("Player login succeess [Name: "+player.getPlayerName()+"]");
					notifyPlayerLoggedIn(player);
				} else {
					Log.info("Player login failed [e-mail:"+email+", password:"+password+"]");
				}
				listenerCallback.onSuccess(player);
			}
		};
		spelstegenService.logIn(email, password, callback);
	}
	
	@Override
	public void logOut() {
		notifyPlayerLoggedOut();
	}
	
	private void notifyPlayerLoggedIn(Player player) {
		for (LoginListener listener : loginListeners) {
			listener.loggedIn(player);
		}
	}
	
	private void notifyPlayerLoggedOut() {
		for (LoginListener listener : loginListeners) {
			listener.loggedOut();
		}
	}

	@Override
	public void getLeague(final Long id) {
		final AsyncCallback<League> callback = new AsyncCallback<League>() {
			public void onFailure(Throwable e) {
				Log.error("Unexpected error when trying to get league [Id: "+id+"]: " + e.getMessage(), e);
			}
			public void onSuccess(League league) {
				if (league != null) {
					Log.debug("League received [Id: "+id+", Name: "+league.getName()+"]");
					notifyLeagueUpdated(league);
					getMatches(league);
				} else {
					Log.warn("No League with id found [Id: "+id+"]");
				}
			}
		};
		
		spelstegenService.getLeague(id, callback);
	}

	@Override
	public void getLeagueSummaries(final AsyncCallback<List<LeagueSummary>> listenerCallback) {
		final AsyncCallback<List<LeagueSummary>> callback = new AsyncCallback<List<LeagueSummary>>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to get summaries of all leagues: " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(List<LeagueSummary> leagueSummaryList) {
				if (leagueSummaryList != null) {
					Log.debug("Received league summeries. Total leagues: " + leagueSummaryList.size());
				} else {
					Log.warn("Received a league summery list that was null");
				}
				listenerCallback.onSuccess(leagueSummaryList);
			}
		};
		spelstegenService.getLeagueSummaries(callback);
	}

	@Override
	public void getLeagues(final Player player, final AsyncCallback<List<League>> listenerCallback) {
		final AsyncCallback<List<League>> callback = new AsyncCallback<List<League>>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to get summaries of all leagues: " + e.getMessage(), e);
				if (listenerCallback != null) {
					listenerCallback.onFailure(e);
				}
			}
			public void onSuccess(List<League> leagues) {
				if (leagues != null) {
					Log.debug("Received leagues for player [Id: "+player.getId()+", Name: "+player.getPlayerName()+"]. Total leagues: " + leagues.size());
				} else {
					Log.warn("Received a league list that was null for player [Id: "+player.getId()+", Name: "+player.getPlayerName()+"]");
				}
				if (listenerCallback != null) {
					listenerCallback.onSuccess(leagues);
				}
			}
		};
		spelstegenService.getLeagues(player, callback);
	}

	private void getMatches(final League league) {
		final AsyncCallback<List<Match>> callback = new AsyncCallback<List<Match>>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to get matches for league [Id:"+league.getId()+", Name:"+league.getName()+"]: " + e.getMessage(), e);
			}
	
			public void onSuccess(List<Match> matches) {
				if (matches != null) {
					Log.debug("Received matches for league [Id:"+league.getId()+", Name:"+league.getName()+"]. Number of matches: " + matches.size());
					notifyMatchListUpdateListeners(matches);
				} else {
					Log.warn("Received a match list that was null for league [Id:"+league.getId()+", Name:"+league.getName()+"]");
				}
			}    	
		};
		spelstegenService.getMatches(league, callback);
	}
	
	public void addMatchListUpdateListener(MatchListUpdateListener listener) {
		matchListUpdateListeners.add(listener);
	}
	
	private void notifyMatchListUpdateListeners(List<Match> matches) {
		for (MatchListUpdateListener listener : matchListUpdateListeners) {
			listener.matchListUpdated(matches);
		}
	}
	
	@Override
	public void saveMatch(final Match match, final Player playerToSave, final League league,
			final AsyncCallback<Void> listenerCallback) {
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to save match ["+match.toString()+"]: " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(Void arg) {
				Log.debug("Match ["+match.toString()+"] successfully saved by player [Id: "+playerToSave.getId()+", Name: "+playerToSave.getPlayerName()+"]");
				listenerCallback.onSuccess(arg);
				getMatches(league);
			}
		};
		spelstegenService.saveMatch(match, playerToSave, callback);
	}

	@Override
	public void getPlayers(final AsyncCallback<List<Player>> listenerCallback) {
		final AsyncCallback<List<Player>> callback = new AsyncCallback<List<Player>>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to get players : " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(List<Player> players) {
				if (players != null) {
					Log.debug("Received list with all players. Number of players: " + players.size());
				} else {
					Log.warn("Received a player list that was null.");
				}
				listenerCallback.onSuccess(players);
			}
		};
		spelstegenService.getPlayers(callback);
	}
	
	@Override
	public void savePlayer(final Player player, final League league, final AsyncCallback<Void> listenerCallback) {
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to save player [Id: "+player.getId()+", Name: "+player.getPlayerName()+"]: " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(Void arg) {
				Log.debug("Player [Id: "+player.getId()+", Name: "+player.getPlayerName()+"] successfully saved.");
				listenerCallback.onSuccess(arg);
				if (league != null) {
					// Refresh current league if player should be a member of the league
					getLeague(league.getId());
				}
			}
		};
		spelstegenService.savePlayer(player, callback);
	}

	@Override
	public void getVersionString(final AsyncCallback<String> listenerCallback) {
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable e) {
				Log.error("Failed to get version string: " + e.getMessage(), e);
				listenerCallback.onFailure(e);
			}
			public void onSuccess(String version) {
				Log.debug("Version reveived: " + version);
				listenerCallback.onSuccess(version);
			}
		};
		spelstegenService.getVersionString(callback);
	}
}
