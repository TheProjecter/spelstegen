package spelstegen.server;

import java.util.List;

import spelstegen.client.entities.League;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.Player;

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
		List<Player> players = storage.getPlayers(-1);
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
		Player p = storage.getPlayer(1);
		assertNotNull(p);
	}

	public void testGetPlayerString() {
		Player p = storage.getPlayer("h@h.se");
		assertNotNull(p);
	}

	public void testGetSports() {
		fail("Not yet implemented");
	}
	
	public void testGetLeagues() {
		List<League> leagues = storage.getLeagues(null);
		for (League league : leagues) {
			System.out.println("league.getName() = " + league.getName());
		}
	}

}
