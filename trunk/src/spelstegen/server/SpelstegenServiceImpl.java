package spelstegen.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import spelstegen.client.LadderCalculator;
import spelstegen.client.SpelstegenService;
import spelstegen.client.entities.League;
import spelstegen.client.entities.LeagueSummary;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.MatchDrawException;
import spelstegen.client.entities.Player;

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

	public void addMatch(Match match, int leagueId) {
		storage.addMatch(match, leagueId);
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
		logger.debug("getLeagues called");
		List<League> leagues = storage.getLeagues(player);
		for (League league : leagues) {
			// Call getMatches to calculate player scores for league
			getMatches(league);
		}
		
		return leagues;
	}

	@Override
	public League getLeague(int id) {
		League league = storage.getLeague(id);
		getMatches(league);
		return league;
	}

	@Override
	public List<LeagueSummary> getLeagueSummaries() {
		return storage.getLeagueSummaries();
	}

}
