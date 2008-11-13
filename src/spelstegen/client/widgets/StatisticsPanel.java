package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdater;

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

	public StatisticsPanel(LeagueUpdater leagueUpdater) {
		setWidth("100%");
		scoreHistoryPanel = new ScoreHistoryPanel(leagueUpdater);
		playerStatisticsPanel = new PlayerStatisticsPanel();
		headToHeadComparisonPanel = new HeadToHeadComparisonPanel();
		add(scoreHistoryPanel, "Poänghistorik");
		add(playerStatisticsPanel, "Toppnoteringar");
		add(headToHeadComparisonPanel, "Inbördes möten");
		selectTab(0);
	}
}
