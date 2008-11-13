/**
 * 
 */
package spelstegen.client;

/**
 * Interface that should be implemented by classes who wants
 * to be notified when current league is updated.
 * 
 * @author Per Mattsson
 */
public interface LeagueUpdateListener {

	/**
	 * Called when league is updated
	 * 
	 * @param league league
	 */
	public void leagueUpdated(League league);
}
