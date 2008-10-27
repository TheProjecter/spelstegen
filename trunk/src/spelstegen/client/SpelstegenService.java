package spelstegen.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SpelstegenService extends RemoteService {

	public boolean logIn(String email, String password);
	
}
