package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LoginListener;
import spelstegen.client.MainApplication;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.entities.League;
import spelstegen.client.entities.Player;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This panel is the container for all the league related content.
 * 
 * @author Henrik Segesten
 */
public class LeaguePanel extends Composite implements LeagueUpdateListener, LoginListener {
	
	private Label leagueNameLabel;
	private TabPanel tabPanel = new TabPanel();
	private StatisticsPanel statisticsPanel;
	private LeagueTablePanel mainLeaguePanel;
	private MatchesTable matchesTable;
	private LeagueAdminPanel leagueAdminPanel;
	private PushButton inputMatchButton;
	private League league;
	private Player loggedInPlayer;
	

	public LeaguePanel(final MainApplication parent, final SpelstegenServiceAsync spelstegenService) {
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
		statisticsPanel = new StatisticsPanel(parent);
		mainLeaguePanel = new LeagueTablePanel(parent);
		matchesTable = new MatchesTable(spelstegenService, parent);
		leagueAdminPanel = new LeagueAdminPanel(spelstegenService, parent);
		parent.addLoginListener(leagueAdminPanel);
		tabPanel.add(mainLeaguePanel, "Tabell");
		tabPanel.add(matchesTable, "Matcher");
		tabPanel.add(statisticsPanel, "Statistik");
		tabPanel.add(leagueAdminPanel, "Administration");
		tabPanel.selectTab(0);
		tabPanel.getDeckPanel().setSize("780px", "500px");
		
		// Bottom buttons
		inputMatchButton = new PushButton("Registrera match");
		inputMatchButton.setEnabled(false);
		inputMatchButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final RegisterResultPanel registerResultPanel = new RegisterResultPanel(spelstegenService, league, 
						parent, loggedInPlayer);
				registerResultPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            registerResultPanel.setPopupPosition(left + 100, top);
			          }
				});
			}
		});
		
		
		HorizontalPanel leagueButtons = MainApplication.createStandardHorizontalPanel();
		leagueButtons.add(inputMatchButton);
		mainPanel.add(leagueButtons);
		mainPanel.add(tabPanel);
		
		
		initWidget(mainPanel);
	}

	public void leagueUpdated(League league) {
		leagueNameLabel.setText(league.getName());
		this.league = league;
	}

	public void loggedIn(Player player) {
		this.loggedInPlayer = player;
		inputMatchButton.setEnabled(true);
	}

	public void loggedOut() {
		inputMatchButton.setEnabled(false);
	}
	
}
