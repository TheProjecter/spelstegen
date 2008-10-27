package spelstegen.server;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import spelstegen.client.SpelstegenService;

/**
 * Main server class of spelstegen.
 * This class provides all operations on the database. It uses Spring to 
 * communicate with the database.
 * @author Henrik Segesten
 *
 */

public class SpelstegenServiceImpl implements SpelstegenService {
	
	private final String PLAYERS_TABLE = "players";
	private final String MATCHES_TABLE = "matches";
	
	private final String PLAYER_ID = "id";
	private final String PLAYER_NAME = "name";
	private final String PLAYER_EMAIL = "email";
	private final String PLAYER_PASSWORD = "password";
	
	
	private String host = "localhost";
	private String port = "3306";
	private String dbname = "spelstegen";
	private String username = "stegusr";
	private String password = "stegpw";
	private SimpleJdbcTemplate simpleJdbcTemplate;

	private void initDB() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	public boolean logIn(String email, String password) {
		String sql = "SELECT " + PLAYER_PASSWORD + " from " + PLAYERS_TABLE + " where " + PLAYER_EMAIL + " = " + email;
		return false;
	}

}
