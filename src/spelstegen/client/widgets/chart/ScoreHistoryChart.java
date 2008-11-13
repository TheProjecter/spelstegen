package spelstegen.client.widgets.chart;

import spelstegen.client.League;
import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LeagueUpdater;

/**
 * Score history chart
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryChart extends AbstractChart implements LeagueUpdateListener {

    public ScoreHistoryChart(LeagueUpdater leagueUpdater) {
        super();
        leagueUpdater.addLeagueUpdateListener(this);
    }

	public void leagueUpdated(League league) {
		// Make the call to the chart generator service.
        chartGeneratorService.generateScoreHistoryChart(league, null, callback);
	}
}
