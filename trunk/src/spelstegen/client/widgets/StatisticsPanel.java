package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdater;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * Statistics panel
 * 
 * @author Per Mattsson
 */
public class StatisticsPanel extends TabPanel {
	
	public static final int WIDTH = 780;
	public static final int HEIGHT = 500;
	private static final int PADDING = 5;
	public static final int TAB_PANEL_WIDTH = WIDTH - (2 * PADDING);
	public static final int TAB_PANEL_HEIGHT = HEIGHT - (2 * PADDING);
	private static final int TAB_BAR_HEIGHT = 40;
	
	private ScoreHistoryPanel scoreHistoryPanel;
	private PlayerStatisticsPanel playerStatisticsPanel;
	private HeadToHeadComparisonPanel headToHeadComparisonPanel;

	public StatisticsPanel(LeagueUpdater leagueUpdater, int width, int height) {
		this.getDeckPanel().setSize(String.valueOf(WIDTH) + "px", String.valueOf(HEIGHT) + "px");
		this.getDeckPanel().setStyleName("statistics");
		int tabPanelWidth = width - (2 * PADDING);
		int tabPanelHeight = height - (2 * PADDING) - TAB_BAR_HEIGHT;
		scoreHistoryPanel = new ScoreHistoryPanel(leagueUpdater, tabPanelWidth, tabPanelHeight);
		playerStatisticsPanel = new PlayerStatisticsPanel();
		headToHeadComparisonPanel = new HeadToHeadComparisonPanel(leagueUpdater);
		add(scoreHistoryPanel, "Poänghistorik");
		add(playerStatisticsPanel, "Toppnoteringar");
		add(headToHeadComparisonPanel, "Inbördes möten");
		selectTab(0);
	}
}
