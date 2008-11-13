/**
 * 
 */
package spelstegen.client;

/**
 * Interface to class that handles league updates and notification to other classes when a
 * league has been updated.
 * 
 * @author Per Mattsson
 */
public interface LeagueUpdater {

	/**
	 * Registers a league update listener.
	 */
	public void addLeagueUpdateListener(LeagueUpdateListener listener);
	
	/**
	 * Triggers a league update
	 */
	public void updateLeague();
}
