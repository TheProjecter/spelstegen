package com.appspot.spelstegen.client.widgets.league;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Player.LeagueRole;
import com.appspot.spelstegen.client.services.LeagueUpdateListener;
import com.appspot.spelstegen.client.services.LoginListener;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LeagueAdminPanel extends Composite implements FocusHandler, LeagueUpdateListener, LoginListener {
	
	private String[] headers = new String [] {"Namn", "E-post", "Smeknamn", "LigAdmin", "MatchAdmin"};
	private SuggestBox addPlayerBox;
	private MultiWordSuggestOracle addPlayerOracle = new MultiWordSuggestOracle();
	private Map<String, Player> allPlayers;
	private GetPlayerCallback getPlayerCallback = new GetPlayerCallback();
	private League league;
	private Player loggedInPlayer;
	private CheckBox leagueAdminCheckBox;
	private Button addPlayerButton;
	private Grid playerGrid;
	private CheckBox matchAdminCheckBox;
	
	public LeagueAdminPanel() {

		ServiceManager.getInstance().addLeagueUpdateListener(this);
		ServiceManager.getInstance().addLoginListener(this);
		allPlayers = new HashMap<String, Player>();
		VerticalPanel mainPanel = Spelstegen.createStandardVerticalPanel();

		
		playerGrid = new Grid(1 + 1, headers.length);
		for (int i = 0; i < headers.length; i++) {
			playerGrid.setText(0, i, headers[i]);
			playerGrid.getCellFormatter().setStyleName(0, i, "table-caption");
		}
		
		VerticalPanel playerTablePanel = Spelstegen.createStandardVerticalPanel();
		playerTablePanel.add(new Label("Spelare i ligan"));
		playerTablePanel.add(playerGrid);
		
		// TODO Basic functionality for displaying the list of players, still working on the db side.
//		mainPanel.add(playerTablePanel);
		
		// Add a new player to league
		Label addPanelLabel = new Label("Lägg till spelare till ligan");
		addPanelLabel.setStylePrimaryName("leagueLabel");
		
		VerticalPanel addPanel = Spelstegen.createStandardVerticalPanel();
		addPanel.add(addPanelLabel);
		
		HorizontalPanel playerAddPanel = Spelstegen.createStandardHorizontalPanel();
		playerAddPanel.add(new Label("Skriv in spelarens epostadress."));
		   
		addPlayerBox = new SuggestBox(addPlayerOracle);
		addPlayerBox.getTextBox().addFocusHandler(this);
		playerAddPanel.add(addPlayerBox);
		
		leagueAdminCheckBox = new CheckBox("L");
		leagueAdminCheckBox.setTitle("Ska den här spelaren vara administratör för ligan?");
		playerAddPanel.add(leagueAdminCheckBox);
		
		matchAdminCheckBox = new CheckBox("M");
		matchAdminCheckBox.setTitle("Ska den här spelaren kunna administrera matcher i ligan?");
		playerAddPanel.add(matchAdminCheckBox);
		
		addPlayerButton = new Button("Lägg till");
		addPlayerButton.setEnabled(false);
		final AsyncCallback<Void> addPlayerCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				Window.alert("Failed to add player to league: " + arg0.getMessage());
			}
			public void onSuccess(Void arg0) {
				Spelstegen.showMessage("Spelaren tillagd.", false);
			}
		};
		
		addPlayerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String email = addPlayerBox.getText().trim();
				Player player = allPlayers.get(email);
				if (player == null) {
					player = new Player(email, email);
				} 
				Set<LeagueRole> leagueRoles = player.getLeagueRoles(league.getId());
				leagueRoles.add(LeagueRole.MEMBER);
				boolean isLeagueAdmin = leagueAdminCheckBox.getValue();
				boolean isMatchAdmin = matchAdminCheckBox.getValue();
				if (isLeagueAdmin) {
					leagueRoles.add(LeagueRole.LEAGUE_ADMIN);
				}
				if (isMatchAdmin) {
					leagueRoles.add(LeagueRole.MATCH_ADMIN);
				}
				player.setLeagueRoles(league.getId(), leagueRoles);
				ServiceManager.getInstance().savePlayer(player, league, addPlayerCallback);
 			}
		});
		
		playerAddPanel.add(addPlayerButton);
		
		mainPanel.add(playerAddPanel);
		initWidget(mainPanel);
		
	}
	
	private class GetPlayerCallback implements AsyncCallback<List<Player>> {
		public void onFailure(Throwable arg0) {
			Window.alert("Failed to get players: " + arg0.getMessage());
		}

		public void onSuccess(List<Player> result) {
			allPlayers.clear();
			for (Player player : result) {
				addPlayerOracle.add(player.getEmail());
				allPlayers.put(player.getEmail(), player);
			}
		}
	}

	public void leagueUpdated(League league) {
		this.league = league;
		if (loggedInPlayer != null) {
			addPlayerButton.setEnabled( loggedInPlayer.isMatchAdmin(league.getId()) );
		}
	}

	public void loggedIn(Player player) {
		loggedInPlayer = player;
		if (league != null) {
			addPlayerButton.setEnabled( loggedInPlayer.isMatchAdmin(league.getId()) );
		}
	}

	public void loggedOut() {
		addPlayerButton.setEnabled(false);
	}

	@Override
	public void onFocus(FocusEvent event) {
		Widget sender = (Widget) event.getSource();
		if (sender == addPlayerBox) {
			ServiceManager.getInstance().getPlayers(getPlayerCallback);
		}
	}
	
}
