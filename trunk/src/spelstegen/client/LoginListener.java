package spelstegen.client;

import spelstegen.client.entities.Player;

public interface LoginListener {

	public void loggedIn(Player player);
	
	public void loggedOut();
	
}
