package spelstegen.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class MatchUTest extends TestCase {
	
	private List<Player> testPlayerData = new ArrayList<Player> (2);
	
	public MatchUTest() {
		Player p1 = new Player("1", "Åke");
		p1.changePoints(53);
		testPlayerData.add(p1);
		Player p2 = new Player("2", "Elsa");
		p2.changePoints(-34);
		testPlayerData.add(p2);
	}
	
	
	public void testCalculateWinner() throws Exception {
		Match m1 = new Match("HT08", new Date(), testPlayerData.get(0).getEmail(), testPlayerData.get(1).getEmail());
		m1.addSet(15, 1);
		assertEquals(m1.getWinner(), testPlayerData.get(0).getEmail());
		m1.addSet(3, 15);
		assertNull(m1.getWinner());
		m1.addSet(4, 15);
		assertEquals(m1.getWinner(), testPlayerData.get(1).getEmail());
	}
	
}
