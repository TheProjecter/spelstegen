package spelstegen.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SpelstegenService extends RemoteService {

	public Player logIn(String email, String password);
	
	public boolean addPlayer(Player player);
	
}
