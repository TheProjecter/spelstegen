package spelstegen.client;

import java.util.List;
import java.util.Map;

import spelstegen.client.entities.League;
import spelstegen.client.entities.LeagueSummary;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Player.PlayerStatus;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous version of the server interface.
 * @author Henrik Segesten
 */
public interface SpelstegenServiceAsync {

	public void logIn(String email, String password, AsyncCallback<Player> callback);
	
	public void addPlayer(Player player, AsyncCallback<Boolean> callback);
	
	public void updatePlayer(Player player, AsyncCallback<Boolean> callback);
	
	public void getPlayers(AsyncCallback<List<Player>> callback);
	
	public void addMatch(Match match, int leagueId, int playerId, AsyncCallback<Boolean> callback);
	
	public void getMatches(League league, AsyncCallback<List<Match>> callback);
	
	public void getLeagues(Player player, AsyncCallback<List<League>> callback);
	
	public void getLeagueSummaries(AsyncCallback<List<LeagueSummary>> callback);
	
	public void getLeague(int id, AsyncCallback<League> callback);
	
	public void addPlayerToLeague(int leagueId, int playerIdToAdd, int playerIdAdder, AsyncCallback<Void> callback);
	
	public void getPlayerStatus(int leagueId, int playerId, AsyncCallback<PlayerStatus> callback);
	
	public void getLeaguePlayersStatus(int leagueId, AsyncCallback<Map<Integer, PlayerStatus>> callback);
	
	public void addLeageAdmin(int leagueId, int playerToAddId, int playerAddingId, AsyncCallback<Boolean> callback);
	
	public void addLeagueMatchAdmin(int leagueId, int playerToAddId, int playerAddingId, AsyncCallback<Boolean> callback);
	
}
