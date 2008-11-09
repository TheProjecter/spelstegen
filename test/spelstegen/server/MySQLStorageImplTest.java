package spelstegen.server;

import java.util.List;

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
		List<Match> matches = storage.getMatches(1);
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
