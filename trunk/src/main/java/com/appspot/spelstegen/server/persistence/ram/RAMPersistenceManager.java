package com.appspot.spelstegen.server.persistence.ram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Sport;
import com.appspot.spelstegen.client.entities.Player.LeagueRole;
import com.appspot.spelstegen.server.persistence.PersistenceManager;

public class RAMPersistenceManager implements PersistenceManager {
	
	private Long leagueId = 0L;
	private Integer playerId = 0;
	private Integer sportId = 0;
	private Integer matchId = 0;
	
	private Map<Long, League> leagues = new HashMap<Long, League>();
	private Map<Integer, Player> players = new HashMap<Integer, Player>();
	private Set<Sport> sports = new HashSet<Sport>();
	private Map<Long, List<Match>> matches = new HashMap<Long, List<Match>>();
	
	public RAMPersistenceManager() {
		createTestData();
	}
	
	private void createTestData() {
		Sport pingis = createSport("Pingis");
		Sport badminton = createSport("Badminton");
		Sport squash = createSport("Squash");
		Sport tennis = createSport("Tennis");
		Sport racketlon = createSport("Racketlon", pingis, badminton, squash, tennis);
		League squashLeague = createLeague("Telias squashstege", squash);
		League racketlonLeague = createLeague("Epsilons racketlonstege", racketlon);
		Player playerA = createPlayer("Arne Anka", "arne.anka@gmail.com");
		Player playerB = createPlayer("Bertil Banan", "bertil.banan@gmail.com");
		Player playerC = createPlayer("Calle Cloak", "calle.cloak@gmail.com");
		Player playerD = createPlayer("Daniel Dante", "daneil.dante@gmail.com");
		Player playerE = createPlayer("Emil Ericsson", "emil.ericsson@gmail.com");
		Player playerF = createPlayer("Fritte Fritös", "fritte.fritos@gmail.com");
		Player playerG = createPlayer("Guran Gute", "guran.gute@gmail.com");
		addPlayerToLeague(playerA, squashLeague, LeagueRole.MEMBER, LeagueRole.MATCH_ADMIN, LeagueRole.LEAGUE_ADMIN);
		addPlayerToLeague(playerB, squashLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerC, squashLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerD, squashLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerD, racketlonLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerE, racketlonLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerF, racketlonLeague, LeagueRole.MEMBER);
		addPlayerToLeague(playerG, racketlonLeague, LeagueRole.MEMBER, LeagueRole.MATCH_ADMIN, LeagueRole.LEAGUE_ADMIN);
		createMatch(squashLeague, new Date(), squash, playerA, playerC, new com.appspot.spelstegen.client.entities.Set(squash, 11, 2), new com.appspot.spelstegen.client.entities.Set(squash, 11, 8));
		createMatch(squashLeague, new Date(), squash, playerA, playerD, new com.appspot.spelstegen.client.entities.Set(squash, 11, 9));
	}
	
	private Sport createSport(String name, Sport... childSports) {
		Sport sport = new Sport(sportId++, name, null, Arrays.asList(childSports));
		sports.add(sport);
		return sport;
	}
	
	private League createLeague(String name, Sport... sports) {
		League league = new League(name);
		league.setId(leagueId++);
		league.setSports(Arrays.asList(sports));
		leagues.put(league.getId(), league);
		return league;
	}
	
	private Player createPlayer(String name, String email) {
		Player player = new Player(name, email);
		player.setId(playerId++);
		players.put(player.getId(), player);
		return player;
	}
	
	private void addPlayerToLeague(Player player, League league, LeagueRole... leagueRoles) {
		Set<LeagueRole> tleagueRoles = player.getLeagueRoles(league.getId());
		tleagueRoles.addAll( Arrays.asList(leagueRoles) );
		player.setLeagueRoles(league.getId(), tleagueRoles);
	}
	
	private Match createMatch(League league, Date date, Sport sport, Player player1,
			Player player2, com.appspot.spelstegen.client.entities.Set... sets) {
		Match match = new Match(league.getId(), date, sport, player1, player2);
		match.setId(matchId++);
		match.setSets(Arrays.asList(sets));
		List<Match> matchList = matches.get(league.getId());
		if (matchList == null) {
			matchList = new ArrayList<Match>();
			matches.put(league.getId(), matchList);
		}
		matchList.add(match);
		return match;
	}

	@Override
	public League getLeague(Long leagueId) {
		return leagues.get(leagueId);
	}

	@Override
	public List<League> getLeagues(Player player) {
		List<League> playerLeagues = new ArrayList<League>();
		for (Long leagueId : player.getAllLeagueRoles().keySet()) {
			playerLeagues.add( leagues.get(leagueId) );
		}
		return playerLeagues;
	}
	
	@Override
	public Collection<League> getAllLeagues() {
		return leagues.values();
	}

	@Override
	public List<Match> getMatches(Long leagueId) {
		List<Match> allMatchesInLeague = matches.get(leagueId);
		if (allMatchesInLeague == null) {
			allMatchesInLeague = new ArrayList<Match>();
		}
//		for (Match match : allMatchesInLeague) {
//			match.getPlayer1().setPoints(1000);
//			match.getPlayer2().setPoints(1000);
//		}
		return allMatchesInLeague;
	}

	@Override
	public Player getPlayer(Integer id) {
		return players.get(id);
	}

	@Override
	public Player getPlayer(String email) {
		if (email == null) {
			throw new IllegalArgumentException("null value for email is not allowed");
		}
		for (Player player : players.values()) {
			if (email.equals(player.getEmail())) {
				return player;
			}
		}
		return null;
	}

	@Override
	public List<Player> getPlayers(Long leagueId) {
		List<Player> playersInLeague = new ArrayList<Player>();
		for (Player player : players.values()) {
			if (player.isLeagueMember(leagueId)) {
				playersInLeague.add(player);
			}
		}
		return playersInLeague;
	}
	
	@Override
	public Collection<Player> getAllPlayers() {
		return players.values();
	}

	@Override
	public List<Sport> getSports() {
		return new ArrayList<Sport>(sports);
	}

	@Override
	public void storeLeague(League league) {
		if (league == null) {
			throw new IllegalArgumentException("null value for league is not allowed");
		}
		if (league.getId() == null) {
			league.setId(leagueId++);
		}
		leagues.put(league.getId(), league);
	}

	@Override
	public void storeMatch(Match match) {
		if (match == null) {
			throw new IllegalArgumentException("null value for match is not allowed");
		}
		if (match.getId() == null) {
			match.setId(matchId++);
		}
		List<Match> matchesInLeague = matches.get(match.getLeagueId());
		if (matchesInLeague == null) {
			matchesInLeague = new ArrayList<Match>();
			matches.put(match.getLeagueId(), matchesInLeague);
		}
		matchesInLeague.add(match);
			
	}

	@Override
	public void storePlayer(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("null value for player is not allowed");
		}
		if (player.getId() == null) {
			player.setId(playerId++);
		}
		players.put(player.getId(), player);
	}
}
