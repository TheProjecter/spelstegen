package spelstegen.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import spelstegen.client.League;
import spelstegen.client.Match;
import spelstegen.client.Player;
import spelstegen.client.ScoreHistory;
import spelstegen.client.Set;
import spelstegen.client.Sport;

public class MySQLStorageImpl implements StorageInterface {

	// Players
	private static final String PLAYERS_TABLE = "players";
	private static final String PLAYER_ID = "id";
	private static final String PLAYER_NAME = "name";
	private static final String PLAYER_EMAIL = "email";
	private static final String PLAYER_PASSWORD = "password";
	private static final String PLAYER_NICKNAME = "nickname";
	private static final String PLAYER_IMAGE = "image_url";
	
	/**
	 * Row mapper for players
	 */
	private class PlayerRowMapper implements ParameterizedRowMapper<Player> {
		@Override
		public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
			Player player = new Player();
			player.setId(rs.getInt(PLAYER_ID));
			player.setPlayerName(rs.getString(PLAYER_NAME));
			player.setNickName(rs.getString(PLAYER_NICKNAME));
			player.setEmail(rs.getString(PLAYER_EMAIL));
			player.setImageURL(rs.getString(PLAYER_IMAGE));
			player.setEncryptedPassword(rs.getString(PLAYER_PASSWORD));
			return player;
		}		
	}
	
	// Matches
	private static final String MATCHES_TABLE = "matches";
	private static final String MATCH_ID = "id";
	private static final String MATCH_DATE = "date";
	private static final String MATCH_SPORT_ID = "sport_id";
	private static final String MATCH_LEAGUE_ID = "league_id";
	private static final String MATCH_PLAYER1_ID = "player1_id";
	private static final String MATCH_PLAYER2_ID = "player2_id";
	
	// Sets
	private static final String SETS_TABLE = "sets";
	private static final String SETS_ID = "id";
	private static final String SETS_SPORT_ID = "sport_id";
	private static final String SETS_PLAYER1_SCORE = "player1Score";
	private static final String SETS_PLAYER2_SCORE = "player2Score";
	private static final String SETS_MATCH_ID = "match_id";
	
	// Sports
	private static final String SPORTS_TABLE = "sports";
	private static final String SPORTS_ID = "id";
	private static final String SPORTS_NAME = "name";
	private static final String SPORTS_ICON_URL = "iconUrl";
	
	/**
	 * Row mapper for sports
	 */
	private class SportRowMapper implements ParameterizedRowMapper<Sport> {
		@Override
		public Sport mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sport sport = new Sport();
			sport.setId(rs.getInt(SPORTS_ID));
			sport.setName(rs.getString(SPORTS_NAME));
			sport.setIconUrl(rs.getString(SPORTS_ICON_URL));
			return sport;
		}		
	}
	
	// Child sports
	private static final String CHILD_SPORTS_TABLE = "childSports";
	private static final String CHILD_SPORTS_PARENT_SPORT_ID = "parentSportId";
	private static final String CHILD_SPORTS_CHILD_SPORT_ID = "childSportId";
	
	// Leagues
	private static final String LEAGUES_TABLE = "leagues";
	private static final String LEAGUES_ID = "id";
	private static final String LEAGUES_NAME = "name";
	
	private class LeagueRowMapper implements ParameterizedRowMapper<League> {

		@Override
		public League mapRow(ResultSet rs, int rowNum) throws SQLException {
			League league = new League();
			league.setId(rs.getInt(LEAGUES_ID));
			league.setName(rs.getString(LEAGUES_NAME));
			return league;
		}
		
	}
	
	// League players
	private static final String LEAGUE_PLAYERS_TABLE = "leaguePlayers";
	private static final String LEAGUE_PLAYERS_LEAGUE_ID = "league_id";
	private static final String LEAGUE_PLAYERS_PLAYER_ID = "player_id";
	
	// League sports
	private static final String LEAGUE_SPORTS_TABLE = "leagueSports";
	private static final String LEAGUE_SPORTS_LEAGUE_ID = "leagueId";
	private static final String LEAGUE_SPORTS_SPORT_ID = "sportId";
	
	// Seasons
	private static final String SEASONS_TABLE = "seasons";
	private static final String SEASONS_ID = "id";
	private static final String SEASONS_NAME = "name";
	private static final String SEASONS_START_DATE = "startDate";
	private static final String SEASONS_END_DATE = "endDate";
	
	// League seasons
	private static final String LEAGUE_SEASONS_TABLE = "leagueSeasons";
	private static final String LEAGUE_SEASONS_LEAGUE_ID = "league_id";
	private static final String LEAGUE_SEASONS_SEASON_ID = "season_id";
	
	private String host = "localhost";
	private String port = "3306";
	private String dbname = "spelstegen";
	private String username = "stegusr";
	private String password = "stegpw";
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	private Log logger = LogFactory.getLog("spelstegenservice.server");
	
	public MySQLStorageImpl() {
		initDB();
	}
	
	private void initDB() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	@Override
	public boolean addPlayer(Player player) {
		String sql = "insert into " + PLAYERS_TABLE + "(" + PLAYER_NAME + "," + PLAYER_EMAIL + "," + PLAYER_PASSWORD 
						+ "," + PLAYER_NICKNAME + "," + PLAYER_IMAGE + ") values(?,?,?,?,?)";
		simpleJdbcTemplate.update(sql, player.getPlayerName(), player.getEmail(), player.getEncryptedPassword(), 
				player.getNickName(), player.getImageURL());
		// TODO check if email is unique and return false.
		return true;
	}
	
	@Override
	public boolean updatePlayer(Player player) {
		String sql = "UPDATE " + PLAYERS_TABLE + " SET " + PLAYER_NAME + "=?," + PLAYER_EMAIL + "=?," + PLAYER_PASSWORD
		 + "=?," + PLAYER_NICKNAME + "=?," + PLAYER_IMAGE + "=? WHERE " + PLAYER_ID + "=?";
		simpleJdbcTemplate.update(sql, player.getPlayerName(), player.getEmail(), player.getEncryptedPassword(), 
				player.getNickName(), player.getImageURL(), player.getId());
		// TODO check if email is unique and return false.
 		return true;
	}

	@Override
	public List<Player> getPlayers() {
		String sql = "select * from players";
		return simpleJdbcTemplate.query(sql, new PlayerRowMapper());
	}

	@Override
	public void addMatch(Match match, League league) {
		/*String sql = "insert into " + MATCHES_TABLE + "(" + MATCH_DATE + "," + MATCH_PLAYER1_ID + "," + MATCH_PLAYER2_ID
			+ "," + MATCH_SETS + "," + MATCH_SEASON + ") values(?,?,?,?,?)";
		simpleJdbcTemplate.update(sql, match.getDate(), match.getPlayers()[0], match.getPlayers()[1], 
				match.getScores(false), match.getSeason());*/
	}
	
	@Override
	public Player getPlayer(int id) {
		String sql = "select * from " + PLAYERS_TABLE + " where " + PLAYER_ID + "=?";
		List<Player> players = simpleJdbcTemplate.query(sql, new PlayerRowMapper(), id);
		if (players.isEmpty())
			return null;
		return players.get(0);
	}

	@Override
	public List<Match> getMatches(League league) {
		
		String sql = "select * from " + MATCHES_TABLE;;
		Object param = null;
		if (league != null) {
			sql += " where " + MATCH_LEAGUE_ID + "=?";
			param = new Object[] {new Integer(league.getId())};
		} else {
			sql = "select * from " + MATCHES_TABLE;
			param = new Object[] {};
		}
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(sql, param);
		List<Match> matches = new ArrayList<Match>(result.size());
		for (Map<String, Object> map : result) {
			Match match = new Match();
			match.setId( (Integer)map.get(MATCH_ID) );
			match.setDate( (Date)map.get(MATCH_DATE) );
			// Get player from league to assure same instance 
			match.setPlayer1( getPlayerFromLeague(league, (Integer)map.get(MATCH_PLAYER1_ID)) );
			match.setPlayer2( getPlayerFromLeague(league, (Integer)map.get(MATCH_PLAYER2_ID)) );
			match.setSport( getSport( (Integer)map.get(MATCH_SPORT_ID) ) );
			match.setSets( getSets(match.getId()) );
			matches.add(match);
		}
		return matches;
	}

	@Override
	public Player getPlayer(String email) {
		String sql = "select * from " + PLAYERS_TABLE + " where " + PLAYER_EMAIL + "=?";
		List<Player> players = simpleJdbcTemplate.query(sql, new PlayerRowMapper(), email);
		if (players.isEmpty())
			return null;
		else
			return players.get(0);
	}

	@Override
	public List<Sport> getSports() {
		String sql = "select * from " + SPORTS_TABLE;
		return simpleJdbcTemplate.query(sql, new SportRowMapper());
	}
	
	/**
	 * Returns the sport with a specified id.
	 * 
	 * @param id id
	 * @return Sport object if found; otherwise null.
	 */
	private Sport getSport(int id) {
		String sql = "select * from " + SPORTS_TABLE + " where "+ SPORTS_ID + "=" + id;
		List<Sport> sports = simpleJdbcTemplate.query(sql, new SportRowMapper());
		if (sports.isEmpty())
			return null;
		Sport sport = sports.get(0);
		// Look for child sports
		sql = "SELECT sports.id, sports.name, sports.iconUrl FROM childsports INNER JOIN sports ON childsports.childSportId = sports.id WHERE (childsports.parentSportId="+ id +") GROUP BY sports.id, sports.name, sports.iconUrl";
		List<Sport> childSports = simpleJdbcTemplate.query(sql, new SportRowMapper());
		sport.setChildSports(childSports);
		return sport;
	}
	
	/**
	 * Returns a list of Set objects that belongs to a specific match.
	 *  
	 * @param matchId id och match
	 * @return A list with all sets that belongs to the match
	 */
	private List<Set> getSets(int matchId) {
		String sql = "select * from sets where match_id=" + matchId;
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(sql);
		List<Set> sets = new ArrayList<Set>(result.size());
		for (Map<String, Object> map : result) {
			Set set = new Set();
			set.setId( (Integer)map.get(SETS_ID) );
			set.setPlayer1Score( (Integer)map.get(SETS_PLAYER1_SCORE) );
			set.setPlayer2Score( (Integer)map.get(SETS_PLAYER2_SCORE) );
			set.setSport( getSport( (Integer)map.get(SETS_SPORT_ID) ) );
			sets.add(set);
		}
		return sets;
	}

	@Override
	public List<League> getLeagues(Player player) {
		StringBuilder buffer = new StringBuilder("SELECT leagues.id, leagues.name FROM (leagues INNER JOIN leagueplayers " +
				"ON leagues.id = leagueplayers.league_id) INNER JOIN players ON leagueplayers.player_id = players.id ");
		if (player != null) {
			buffer.append("WHERE (leagueplayers.player_id="+player.getId()+")");
		} 
		buffer.append(" GROUP BY leagues.id, leagues.name");
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(buffer.toString());
		List<League> leagues = new ArrayList<League>(result.size());
		for (Map<String, Object> map : result) {
			League league = new League();
			league.setId( (Integer)map.get(LEAGUES_ID) );
			league.setName( (String)map.get(LEAGUES_NAME) );
			String sql = "SELECT players.id, players.name, players.email, players.nickname, players.password, " +
					"players.image_url FROM (leagues INNER JOIN leagueplayers ON leagues.id = leagueplayers.league_id) " +
					"INNER JOIN players ON leagueplayers.player_id = players.id WHERE (leagues.id="+league.getId()+") " +
					"GROUP BY players.id, players.name, players.email, players.nickname, players.password, players.image_url";
			List<Player> players = simpleJdbcTemplate.query(sql, new PlayerRowMapper());
			league.setPlayers(players);
			
			// TODO Add league sports and seasons
			
			leagues.add(league);
		}
		return leagues;
	}

	
	/**
	 * Returns a player with a specified id that is part of a league.
	 * 
	 * @param league the league
	 * @param playerId player id
	 */
	private Player getPlayerFromLeague(League league, int playerId) {
		for (Player player : league.getPlayers()) {
			if (player.getId() == playerId) {
				return player;
			}
		}
		return null;
	}

	@Override
	public List<ScoreHistory> getScoreHistory(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPlayerToLeague(int leagueid, int playerid) {
		String sql = "INSERT INTO " + LEAGUE_PLAYERS_TABLE + " VALUES(?,?)";
		simpleJdbcTemplate.update(sql, leagueid, playerid);
	}



}
