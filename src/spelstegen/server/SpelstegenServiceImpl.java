package spelstegen.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
	
	private final String PLAYERS_TABLE = "players";
	private final String MATCHES_TABLE = "matches";
	
	private final String PLAYER_NAME = "name";
	private final String PLAYER_EMAIL = "email";
	private final String PLAYER_PASSWORD = "password";
	
	private final String MATCH_ID = "id";
	private final String MATCH_DATE = "date";
	private final String MATCH_PLAYER1 = "player1";
	private final String MATCH_PLAYER2 = "player2";
	private final String MATCH_SETS = "sets";
	private final String MATCH_SEASON = "season";
	
	private String host = "localhost";
	private String port = "3306";
	private String dbname = "spelstegen";
	private String username = "stegusr";
	private String password = "stegpw";
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public SpelstegenServiceImpl () {
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
	
	public Player logIn(String email, String password) {
		String sql = "select " + PLAYER_NAME + "," + PLAYER_PASSWORD + " from " + PLAYERS_TABLE + 
						" where " + PLAYER_EMAIL + " = ?";
		
		Map<String, Object> result;
		try {
			result = simpleJdbcTemplate.queryForMap(sql, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (result.get(PLAYER_PASSWORD).equals(password)) {
			Player player = new Player((String)result.get(PLAYER_NAME), email);
			return player;
		}
		return null;
	}

	public boolean addPlayer(Player player) {
		String sql = "insert into " + PLAYERS_TABLE + " values(?,?,?)";
		simpleJdbcTemplate.update(sql, player.getEmail(), player.getPlayerName(), player.getEncryptedPassword());
		// TODO check if email is unique and return false.
		return true;
	}

	public List<Player> getPlayers() {
		String sql = "select " + PLAYER_EMAIL + "," + PLAYER_NAME + " from " + PLAYERS_TABLE;
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(sql, new Object[] {});
		List<Player> output = new ArrayList<Player>(result.size());
		for (Map<String, Object> map : result) {
			output.add(new Player((String)map.get(PLAYER_NAME), (String)map.get(PLAYER_EMAIL)));
		}
		return output;
	}

	public void addMatch(Match match) {
		String sql = "insert into " + MATCHES_TABLE + "(" + MATCH_DATE + "," + MATCH_PLAYER1 + "," + MATCH_PLAYER2
			+ "," + MATCH_SETS + "," + MATCH_SEASON + ") values(?,?,?,?,?)";
		simpleJdbcTemplate.update(sql, match.getDate(), match.getPlayers()[0], match.getPlayers()[1], 
				match.getScores(false), match.getSeason());
	}

	public List<Match> getMatches(String season) {
		String sql = null;
		if (season != null) {
			sql = "select * from " + MATCHES_TABLE + " where " + MATCH_SEASON + "=?";
		} else {
			sql = "select * from " + MATCHES_TABLE;
		}
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(sql, season);
		List<Match> matches = new ArrayList<Match>(result.size());
		Match m = null;
		for (Map<String, Object> map : result) {
			m = new Match((String)map.get(MATCH_ID), (String)map.get(MATCH_SEASON), (Date)map.get(MATCH_DATE), 
					(String)map.get(MATCH_PLAYER1), (String)map.get(MATCH_PLAYER2));
			String sets = (String) map.get(MATCH_SETS);
			
		}
		return null;
	}

}
