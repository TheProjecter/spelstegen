package spelstegen.server;

import java.util.List;

import spelstegen.client.League;
import spelstegen.client.Match;
import spelstegen.client.Player;

import junit.framework.TestCase;

/**
 * Test case for MySQLStorageImpl
 * 
 * @author Per Mattsson
 */
public class MySQLStorageImplTest extends TestCase {

	private StorageInterface storage;
	
	public MySQLStorageImplTest() {
		storage = new MySQLStorageImpl();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAddPlayer() {
		fail("Not yet implemented");
	}

	public void testGetPlayers() {
		List<Player> players = storage.getPlayers();
		assertNotNull(players);
	}

	public void testAddMatch() {
		fail("Not yet implemented");
	}

	public void testGetMatches() {
		League league = new League();
		league.setId(1);
		List<Match> matches = storage.getMatches(league);
		assertNotNull(matches);
	}

	public void testGetPlayerInt() {
		fail("Not yet implemented");
	}

	public void testGetPlayerString() {
		fail("Not yet implemented");
	}

	public void testGetSports() {
		fail("Not yet implemented");
	}

}