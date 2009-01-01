package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LoginListener;
import spelstegen.client.MainApplication;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.entities.League;
import spelstegen.client.entities.Player;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This panel is the container for all the league related content.
 * 
 * @author Henrik Segesten
 */
public class LeaguePanel extends Composite implements LeagueUpdateListener, LoginListener {
	
	private static final int TAB_BAR_HEIGHT = 40;
	
	private Label leagueNameLabel;
	private TabPanel tabPanel = new TabPanel();
	private StatisticsPanel statisticsPanel;
	private LeagueTablePanel mainLeaguePanel;
	private MatchesTable matchesTable;
	private RegisterResultPanel registerResultPanel;
	private LeagueAdminPanel leagueAdminPanel;
	private League league;
	private Player loggedInPlayer;
	private boolean tabsAreVisible = false;
	

	public LeaguePanel(final MainApplication parent, final SpelstegenServiceAsync spelstegenService, int width, int height) {
		parent.addLeagueUpdateListener(this);
		parent.addLoginListener(this);
		
		// Construct gui
		VerticalPanel mainPanel = MainApplication.createStandardVerticalPanel();
		mainPanel.setWidth("100%");
		//mainPanel.setStyleName("outer-border");
		leagueNameLabel = new Label("", false);
		leagueNameLabel.setStylePrimaryName("leagueLabel");
		mainPanel.add(leagueNameLabel);
		
		// Tab panel
		statisticsPanel = new StatisticsPanel(parent, width, height - TAB_BAR_HEIGHT);
		mainLeaguePanel = new LeagueTablePanel(parent);
		matchesTable = new MatchesTable(spelstegenService, parent);
		registerResultPanel = new RegisterResultPanel(spelstegenService, parent, parent);
		leagueAdminPanel = new LeagueAdminPanel(spelstegenService, parent);
		parent.addLoginListener(leagueAdminPanel);
		tabPanel.add(mainLeaguePanel, "Tabell");
		tabPanel.add(matchesTable, "Matcher");
		tabPanel.add(statisticsPanel, "Statistik");
		tabPanel.selectTab(0);
		tabPanel.getDeckPanel().setSize(String.valueOf(width) + "px", String.valueOf(height) + "px");
		
		HorizontalPanel leagueButtons = MainApplication.createStandardHorizontalPanel();
		mainPanel.add(leagueButtons);
		mainPanel.add(tabPanel);
		
		initWidget(mainPanel);
	}

	public void leagueUpdated(League league) {
		leagueNameLabel.setText(league.getName());
		this.league = league;
		updateTabs();
	}

	public void loggedIn(Player player) {
		this.loggedInPlayer = player;
		updateTabs();
	}

	public void loggedOut() {
		this.loggedInPlayer = null;
		updateTabs();
	}	
	
	private void updateTabs() {
		if ((loggedInPlayer == null) || (league == null) || (!league.getPlayers().contains(loggedInPlayer))) {
			if (tabsAreVisible) {
				tabPanel.remove(4);
				tabPanel.remove(3);
				tabsAreVisible = false;
			}
		} else if (!tabsAreVisible) {
			tabPanel.add(registerResultPanel, "Registrera match");
			tabPanel.add(leagueAdminPanel, "Administration");
			tabsAreVisible = true;
		}
	}
}
