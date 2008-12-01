package spelstegen.client.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * Score history for a player.
 * Player scores are sorted by date.
 * 
 * @author Per Mattsson
 */
public class ScoreHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Player player;
	private List<Score> scoreHistory;
	
	public ScoreHistory() {}
	
	public ScoreHistory(Player player, List<Score> scoreHistory) {
		this.player = player;
		setScoreHistory(scoreHistory);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Score> getScoreHistory() {
		return scoreHistory;
	}

	public void setScoreHistory(List<Score> scoreHistory) {
		this.scoreHistory = scoreHistory;
		Collections.sort(scoreHistory);
	}
}
