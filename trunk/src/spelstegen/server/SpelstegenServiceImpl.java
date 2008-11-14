package spelstegen.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import spelstegen.client.LadderCalculator;
import spelstegen.client.League;
import spelstegen.client.Match;
import spelstegen.client.MatchDrawException;
import spelstegen.client.Player;
import spelstegen.client.SpelstegenService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Main server class of spelstegen.
 * This class provides all operations on the database. It uses Spring to 
 * communicate with the database.
 * 
 * @author Henrik Segesten
 */

public class SpelstegenServiceImpl extends RemoteServiceServlet implements SpelstegenService {
	
	private static final long serialVersionUID = 1L;
	
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
	
	@Override
	public boolean updatePlayer(Player player) {
		return storage.updatePlayer(player);
	}

	public void addMatch(Match match, League league) {
		storage.addMatch(match, league);
	}

	public List<Match> getMatches(League league) {
		List<Match> matches = storage.getMatches(league);
		try {
			LadderCalculator.calculateScore(matches);
		} catch (MatchDrawException e) {
			logger.error("Error calculating player scores", e);
		}
		return matches;
	}

	public List<Player> getPlayers() {
		return storage.getPlayers();
	}

	@Override
	public List<League> getLeagues(Player player) {
		List<League> leagues = storage.getLeagues(player);
		for (League league : leagues) {
			// Call getMatches to calculate player scores for league
			getMatches(league);
		}
		
		return leagues;
	}

}
