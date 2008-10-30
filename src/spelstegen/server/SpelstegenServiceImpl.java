package spelstegen.server;

import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

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
		String sql = "select " + PLAYER_NAME + "," + PLAYER_PASSWORD + " from " + PLAYERS_TABLE + " where " + PLAYER_EMAIL + " = ?";
		
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

}
