package spelstegen.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import spelstegen.client.Match;
import spelstegen.client.Player;
import spelstegen.client.SpelstegenService;

/**
 * Main server class of spelstegen.
 * This class provides all operations on the database. It uses Spring to 
 * communicate with the database.
 * @author Henrik Segesten
 *
 */

public class SpelstegenServiceImpl extends RemoteServiceServlet implements SpelstegenService {
	

	
	private Log logger = LogFactory.getLog("spelstegenservice.server");
	private StorageInterface storage = new MySQLStorageImpl();

	
	public SpelstegenServiceImpl () {
		
	}
	
	public Player logIn(String email, String password) {
		Player p = storage.getPlayer(email);
		if (p.getEncryptedPassword().equals(password)) {
			logger.debug("Player with email: " + email + " logged in.");
			return p;
		}
		logger.warn("Password for player with email: " + email + " was incorrect.");
		return null;
	}

	public boolean addPlayer(Player player) {
		return storage.addPlayer(player);
	}

	public void addMatch(Match match) {
		storage.addMatch(match);
	}

	public List<Match> getMatches(int season) {
		return storage.getMatches(season);
	}

	public List<Player> getPlayers() {
		return storage.getPlayers();
	}



}
