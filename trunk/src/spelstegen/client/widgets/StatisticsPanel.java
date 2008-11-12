package spelstegen.client.widgets;

import spelstegen.client.League;
import spelstegen.client.Season;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * Statistics panel
 * 
 * @author Per Mattsson
 */
public class StatisticsPanel extends TabPanel {
	
	private ScoreHistoryPanel scoreHistoryPanel;
	private PlayerStatisticsPanel playerStatisticsPanel;
	private HeadToHeadComparisonPanel headToHeadComparisonPanel;

	public StatisticsPanel() {
		setWidth("100%");
		scoreHistoryPanel = new ScoreHistoryPanel();
		playerStatisticsPanel = new PlayerStatisticsPanel();
		headToHeadComparisonPanel = new HeadToHeadComparisonPanel();
		add(scoreHistoryPanel, "Poänghistorik");
		add(playerStatisticsPanel, "Toppnoteringar");
		add(headToHeadComparisonPanel, "Inbördes möten");
		selectTab(0);
	}
	
	public void setData(League league, Season season) {
		scoreHistoryPanel.setData(league, season);
	}
}
