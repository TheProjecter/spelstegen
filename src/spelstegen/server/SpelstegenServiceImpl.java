package spelstegen.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import spelstegen.client.LadderCalculator;
import spelstegen.client.SpelstegenService;
import spelstegen.client.entities.League;
import spelstegen.client.entities.LeagueSummary;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.MatchDrawException;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Player.PlayerStatus;

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
	
	@Override
	public Player logIn(String email, String password) {
		Player p = storage.getPlayer(email);
		if (p.getEncryptedPassword().equals(password)) {
			logger.debug("Player with email: " + email + " logged in.");
			return p;
		}
		logger.warn("Password for player with email: " + email + " was incorrect.");
		return null;
	}

	@Override
	public boolean addPlayer(Player player) {
		return storage.addPlayer(player);
	}
	
	@Override
	public boolean updatePlayer(Player player) {
		return storage.updatePlayer(player);
	}

	@Override
	public boolean addMatch(Match match, int leagueId, int playerId) {
		if (storage.isPlayerLeagueMatchAdmin(playerId, leagueId)) {
			storage.addMatch(match, leagueId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Match> getMatches(League league) {
		List<Match> matches = storage.getMatches(league);
		try {
			LadderCalculator.calculateScore(matches);
		} catch (MatchDrawException e) {
			logger.error("Error calculating player scores", e);
		}
		return matches;
	}

	@Override
	public List<Player> getPlayers() {
		return storage.getPlayers(-1);
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

	@Override
	public void addPlayerToLeague(int leagueId, int playerIdToAdd, int playerIdAdder) {
		if (storage.isPlayerLeagueAdmin(playerIdAdder, leagueId)) {
			storage.addPlayerToLeague(leagueId, playerIdToAdd);
		}
	}

	@Override
	public PlayerStatus getPlayerStatus(int leagueId, int playerId) {
		boolean leageAdmin = storage.isPlayerLeagueAdmin(playerId, leagueId);
		boolean matchAdmin = storage.isPlayerLeagueMatchAdmin(playerId, leagueId);
		if (leageAdmin && matchAdmin) {
			return PlayerStatus.SUPER_USER;
		} else if (leageAdmin) {
			return PlayerStatus.LEAGUE_ADMIN;
		} else if (matchAdmin) {
			return PlayerStatus.MATCH_ADMIN;
		} else if (storage.isPlayerInLeague(playerId, leagueId)) {
			return PlayerStatus.MEMBER;
		} else {
			return PlayerStatus.NON_MEMBER;
		}
	}

	@Override
	public Map<Integer, PlayerStatus> getLeaguePlayersStatus(int leagueId) {
		List<Player> players = storage.getPlayers(leagueId);
		List<Integer> leagueAdmins = storage.getAllLeagueAdmins(leagueId);
		List<Integer> matchAdmins = storage.getAllMatchAdmins(leagueId);
		Map<Integer, PlayerStatus> result = new HashMap<Integer, PlayerStatus>(players.size());
		int playerId;
		for (Player player : players) {
			playerId = player.getId();
			if (leagueAdmins.contains(playerId) && matchAdmins.contains(playerId)) {
				result.put(playerId, PlayerStatus.SUPER_USER);
			} else if (leagueAdmins.contains(playerId)) {
				result.put(playerId, PlayerStatus.LEAGUE_ADMIN);
			} else if (matchAdmins.contains(playerId)) {
				result.put(playerId, PlayerStatus.MATCH_ADMIN);
			} else {
				result.put(playerId, PlayerStatus.MEMBER);
			}
		}
		return result;
	}

	@Override
	public boolean addLeageAdmin(int leagueId, int playerToAddId, int playerAddingId) {
		if (storage.isPlayerLeagueAdmin(playerAddingId, leagueId)) {
			storage.addLeagueAdminRoles(playerToAddId, leagueId, true, false);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addLeagueMatchAdmin(int leagueId, int playerToAddId,	int playerAddingId) {
		if (storage.isPlayerLeagueAdmin(playerAddingId, leagueId)) {
			storage.addLeagueAdminRoles(playerAddingId, leagueId, false, true);
			return true;
		} else {
			return false;
		}
	}

}
