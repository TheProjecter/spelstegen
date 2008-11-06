package spelstegen.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import spelstegen.client.Match;
import spelstegen.client.Player;
import spelstegen.client.Sport;
import spelstegen.client.Match.MatchDoneException;

public class MySQLStorageImpl implements StorageInterface {
	
	private final String PLAYERS_TABLE = "players";
	private final String MATCHES_TABLE = "matches";
	
	private final String PLAYER_ID = "id";
	private final String PLAYER_NAME = "name";
	private final String PLAYER_EMAIL = "email";
	private final String PLAYER_PASSWORD = "password";
	private final String PLAYER_NICKNAME = "nickname";
	private final String PLAYER_IMAGE = "image_url";
	
	private final String MATCH_ID = "id";
	private final String MATCH_DATE = "date";
	private final String MATCH_PLAYER1 = "player1";
	private final String MATCH_PLAYER2 = "player2";
	private final String MATCH_SETS = "sets";
	private final String MATCH_SEASON = "season_id";
	
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

	public List<Match> getMatches(int season) throws NumberFormatException {
		String sql = null;
		Object[] param = null;
		if (season != -1) {
			sql = "select * from " + MATCHES_TABLE + " where " + MATCH_SEASON + "=?";
			param = new Object[] {season};
		} else {
			sql = "select * from " + MATCHES_TABLE;
			param = new Object[] {};
		}
		List<Map<String,Object>> result = simpleJdbcTemplate.queryForList(sql, param);
		List<Match> matches = new ArrayList<Match>(result.size());
		Match m = null;
		for (Map<String, Object> map : result) {
			m = new Match((Integer)map.get(MATCH_ID), (Integer)map.get(MATCH_SEASON), (Date)map.get(MATCH_DATE), 
					(String)map.get(MATCH_PLAYER1), (String)map.get(MATCH_PLAYER2));
			String setsString = (String) map.get(MATCH_SETS);
			if (setsString != null) {
				String[] sets = setsString.split(",");
				String[] scores;
				for (String string : sets) {
					scores = string.split("-");
					if (scores.length == 2) {
						try {
							m.addSet(Integer.parseInt(scores[0].trim()), Integer.parseInt(scores[1].trim()));
						} catch (MatchDoneException e) {
							logger.error("Error adding score " + string + " to match. " + e.getMessage());
						}
					}
				}
			}
			matches.add(m);
		}
		return matches;
	}

	public Player getPlayer(int id) {
		String sql = "select * where " + PLAYER_ID + " = ?";
		Map<String, Object> dbOutput;
		try {
			dbOutput = simpleJdbcTemplate.queryForMap(sql, id);
			if (dbOutput != null) {
				return createPlayer(dbOutput);
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return null;
	}

	public Player getPlayer(String email) {
		String sql = "select * where " + PLAYER_EMAIL + " = ?";
		Map<String, Object> dbOutput;
		try {
			dbOutput = simpleJdbcTemplate.queryForMap(sql, email);
			if (dbOutput != null) {
				return createPlayer(dbOutput);
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return null;
	}
	
	private Player createPlayer(Map<String, Object> dbOutput) {
		Player p = new Player();
		p.setId((Integer) dbOutput.get(PLAYER_ID));
		p.setPlayerName((String) dbOutput.get(PLAYER_NAME));
		p.setEmail((String) dbOutput.get(PLAYER_EMAIL));
		p.setNickName((String) dbOutput.get(PLAYER_NICKNAME));
		p.changePassword((String) dbOutput.get(PLAYER_PASSWORD));
		p.setImageURL((String) dbOutput.get(PLAYER_IMAGE));
		return p;
	}

	public List<Sport> getSports() {
		// TODO Auto-generated method stub
		return null;
	}

}
