package com.appspot.spelstegen.server;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Player.LeagueRole;
import com.appspot.spelstegen.client.services.SpelstegenService;
import com.appspot.spelstegen.server.persistence.PersistenceManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Main server class of spelstegen.
 * 
 * This class provides all available services available to the client.
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */

public class SpelstegenServiceImpl extends RemoteServiceServlet implements SpelstegenService {
	
	private static final String POM_PROPERTIES = "/META-INF/maven/com.appspot/spelstegen/pom.properties";

	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private PersistenceManager pm;
	
	public SpelstegenServiceImpl () {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		this.pm = (PersistenceManager) context.getBean("persistenceManager");
		
//		this.pm = new RAMPersistenceManager();
//		this.pm = new MySqlPersistenceManager();
//		this.pm = new JDOPersistenceManager();
	}
	
	@Override
	public Player logIn(String email, String password) {
		Player p = pm.getPlayer(email);
		// TODO: Enable password check
		if (true/*p.getEncryptedPassword().equals(password)*/) {
			logger.log(Level.FINE, "Player with email: " + email + " logged in.");
			return p;
		}
		logger.log(Level.WARNING, "Password for player with email: " + email + " was incorrect.");
		return null;
	}

	@Override
	public void savePlayer(Player player) {
		pm.storePlayer(player);
	}

	@Override
	public void saveMatch(Match match, Player playerToSave) {
		pm.storeMatch(match);
	}

	@Override
	public List<Match> getMatches(League league) {
		List<Match> matches = pm.getMatches(league.getId());
		return matches;
	}

	@Override
	public List<Player> getPlayers() {
		return pm.getPlayers(null);
	}

	@Override
	public List<League> getLeagues(Player player) {
		logger.log(Level.FINE, "getLeagues called");
		List<Long> leagueIds = player.getLeagueIds(LeagueRole.MEMBER);
		List<League> leagues = new ArrayList<League>();
		for (Long leagueId : leagueIds) {
			leagues.add( getLeague(leagueId) );
		}
		return leagues;
	}

	@Override
	public League getLeague(Long id) {
		try {
			League league = pm.getLeague(id);
			List<Player> players = pm.getPlayers(league.getId());
			league.setPlayers(players);
			return league;
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public List<LeagueSummary> getLeagueSummaries() {
		List<LeagueSummary> allLeagueSummaries = new ArrayList<LeagueSummary>();
		Collection<League> allLeagues = pm.getAllLeagues();
		Collection<Player> allPlayers = pm.getAllPlayers();
		for (League league : allLeagues) {
			int nbrOfMembers = 0;
			for (Player player : allPlayers) {
				if (player.isLeagueMember(league.getId())) {
					nbrOfMembers++;
				}
			}
			allLeagueSummaries.add(new LeagueSummary(league.getId(), league
					.getName(), nbrOfMembers));
		}
		return allLeagueSummaries;
	}

	@Override
	public String getVersionString() {
		Properties pomProperties = new Properties();
		try {
			pomProperties.load(getServletContext().getResourceAsStream(POM_PROPERTIES));
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not find " + POM_PROPERTIES);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while reading "+ POM_PROPERTIES +": " + e.getMessage());
		}
		String version = pomProperties.getProperty("version", "?");
		return version;
	}

}
