package spelstegen.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous version of the server interface.
 * @author Henrik Segesten
 */
public interface SpelstegenServiceAsync {

	public void logIn(String email, String password, AsyncCallback<Player> callback);
	
	public void addPlayer(Player player, AsyncCallback<Boolean> callback);
	
	public void getPlayers(AsyncCallback<List<Player>> callback);
	
	public void addMatch(Match match, AsyncCallback<Void> callback);
	
	public void getMatches(int season, AsyncCallback<List<Match>> callback);
	
}
