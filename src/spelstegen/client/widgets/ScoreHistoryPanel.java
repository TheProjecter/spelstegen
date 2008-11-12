/**
 * 
 */
package spelstegen.client.widgets;

import spelstegen.client.League;
import spelstegen.client.Season;
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
	
	public ScoreHistoryPanel() {
		setWidth("100%");
		setHeight("500");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(scoreHistoryChart = new ScoreHistoryChart());
	}
	
	public void setData(League league, Season season) {
		scoreHistoryChart.setData(league, season);
	}
}
