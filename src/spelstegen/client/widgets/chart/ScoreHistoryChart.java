package spelstegen.client.widgets.chart;

import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LeagueUpdater;
import spelstegen.client.entities.League;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Score history chart
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryChart extends AbstractChart implements LeagueUpdateListener {
	
	private AsyncCallback callback;
	
    public ScoreHistoryChart(int width, int height, LeagueUpdater leagueUpdater) {
        super(width, height);
        
        // Setup the callback from the chart generator service.
        callback = new AsyncCallback() {
            /*
             * If the call was successful, we will get back the name of the chart
             * that was created and stored on the server.
             */
            public void onSuccess(Object s) {
                String chartName = (String)s;
                String imageUrl = "./displayChart?filename=" + chartName;
                setUrl(imageUrl);
            }
           
            /*
             * Something went wrong with the call. Handle the issue how you'd like.
             */
            public void onFailure(Throwable ex) {
                // do nothing for now
            }
        };
        leagueUpdater.addLeagueUpdateListener(this);
    }

	public void leagueUpdated(League league) {
		// Make the call to the chart generator service.
        chartGeneratorService.generateScoreHistoryChart(league, null, width, height, callback);
	}
}