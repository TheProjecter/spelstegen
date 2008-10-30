package spelstegen.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SpelstegenServiceAsync {

	public void logIn(String email, String password, AsyncCallback<Player> callback);
	
	public void addPlayer(Player player, AsyncCallback<Boolean> callback);
	
}
