/**
 * 
 */
package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdater;
import spelstegen.client.widgets.chart.ScoreHistoryChart;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Score history panel
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryPanel extends VerticalPanel {

	private final static int VERTICAL_SPACING = 15;
	private ScoreHistoryChart scoreHistoryChart;
	
	public ScoreHistoryPanel(LeagueUpdater leagueUpdater, int width, int height) {
		setWidth("100%");
		setHeight("100%");
		//setHeight(String.valueOf(StatisticsPanel.HEIGHT) + "px");
		//setHeight("700px");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(scoreHistoryChart = new ScoreHistoryChart(width, height, leagueUpdater));
	}
}
