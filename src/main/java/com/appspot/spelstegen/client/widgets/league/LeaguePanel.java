package com.appspot.spelstegen.client.widgets.league;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.LeagueUpdateListener;
import com.appspot.spelstegen.client.services.LoginListener;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.appspot.spelstegen.client.widgets.league.statistics.StatisticsPanel;
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
	private MatchesTablePanel matchesTable;
	private RegisterResultPanel registerResultPanel;
	private LeagueAdminPanel leagueAdminPanel;
	private League currentLeague;
	private Player loggedInPlayer;

	public LeaguePanel(int width, int height) {
		// Construct gui
		VerticalPanel mainPanel = Spelstegen.createStandardVerticalPanel();
		mainPanel.setWidth("100%");
		leagueNameLabel = new Label("", false);
		leagueNameLabel.setStylePrimaryName("leagueLabel");
		mainPanel.add(leagueNameLabel);
		
		// Tab panel
		statisticsPanel = new StatisticsPanel(width, height - TAB_BAR_HEIGHT);
		mainLeaguePanel = new LeagueTablePanel();
		matchesTable = new MatchesTablePanel();
		registerResultPanel = new RegisterResultPanel();
		leagueAdminPanel = new LeagueAdminPanel();
		tabPanel.add(mainLeaguePanel, "Tabell");
		tabPanel.add(matchesTable, "Matcher");
		tabPanel.add(statisticsPanel, "Statistik");
		tabPanel.selectTab(0);
		tabPanel.getDeckPanel().setSize(String.valueOf(width) + "px", String.valueOf(height) + "px");
		
		HorizontalPanel leagueButtons = Spelstegen.createStandardHorizontalPanel();
		mainPanel.add(leagueButtons);
		mainPanel.add(tabPanel);
		
		initWidget(mainPanel);
		
		ServiceManager.getInstance().addLeagueUpdateListener(this);
		ServiceManager.getInstance().addLoginListener(this);
	}

	@Override
	public void leagueUpdated(League league) {
		leagueNameLabel.setText(league.getName());
		this.currentLeague = league;
		updateTabs();
	}

	@Override
	public void loggedIn(Player player) {
		this.loggedInPlayer = player;
		updateTabs();
	}

	@Override
	public void loggedOut() {
		this.loggedInPlayer = null;
		updateTabs();
	}	
	
	private void updateTabs() {
		if (isMatchAdmin(loggedInPlayer, currentLeague)) {
			if (tabPanel.getWidgetIndex(registerResultPanel) < 0) {
				tabPanel.add(registerResultPanel, "Registrera match");
			}
		} else {
			tabPanel.remove(registerResultPanel);
		} 
		
		if (isLeagueAdmin(loggedInPlayer, currentLeague)) {
			if (tabPanel.getWidgetIndex(leagueAdminPanel) < 0) {
				tabPanel.add(leagueAdminPanel, "Administration");
			}
		} else {
			tabPanel.remove(leagueAdminPanel);
		} 
	}

	/**
	 * Determines if a players is allowed to register matches in a specific
	 * league
	 * 
	 * @param player
	 *            the player
	 * @param league
	 *            the league
	 * @return true if player is allowed to register matches; otherwise false
	 */
	private boolean isMatchAdmin(Player player, League league) {
		if ((player == null) || (league == null)) {
			return false;
		}
		return (player.isMatchAdmin(league.getId()));
	}

	/**
	 * Determines if a players is allowed to administrate players in a specific
	 * league
	 * 
	 * @param player
	 *            the player
	 * @param league
	 *            the league
	 * @return true if player is allowed to administrate players; otherwise
	 *         false
	 */
	private boolean isLeagueAdmin(Player player, League league) {
		if ((player == null) || (league == null)) {
			return false;
		}
		return (player.isLeagueAdmin(league.getId()));
	}
}
