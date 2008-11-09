package spelstegen.client.widgets.chart;

import spelstegen.client.League;
import spelstegen.client.Season;

/**
 * Score history chart
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryChart extends AbstractChart {

    public ScoreHistoryChart() {
        super();
    }
    
    public void setData(League league, Season season) {
    	// Make the call to the chart generator service.
        chartGeneratorService.generateScoreHistoryChart(league, null, callback);
    }
}
