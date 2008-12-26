/**
 * 
 */
package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdater;
import spelstegen.client.widgets.chart.PieChart;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Head-to-head comparison panel
 * 
 * @author Per Mattsson
 */
public class HeadToHeadComparisonPanel extends VerticalPanel {

	private final static int VERTICAL_SPACING = 15;
	
	public HeadToHeadComparisonPanel(LeagueUpdater leagueUpdater) {
		setWidth("100%");
		setHeight(String.valueOf(StatisticsPanel.HEIGHT) + "px");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(new PieChart(StatisticsPanel.TAB_PANEL_WIDTH, StatisticsPanel.TAB_PANEL_HEIGHT, leagueUpdater));
	}
}
