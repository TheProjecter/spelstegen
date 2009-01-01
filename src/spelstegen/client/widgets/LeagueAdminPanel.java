package spelstegen.client.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LeagueUpdater;
import spelstegen.client.LoginListener;
import spelstegen.client.MainApplication;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.entities.League;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Player.PlayerStatus;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LeagueAdminPanel extends Composite implements FocusListener, LeagueUpdateListener, LoginListener {
	
	private String[] headers = new String [] {"Namn", "E-post", "Smeknamn", "LigAdmin", "MatchAdmin"};
	private SuggestBox addPlayerBox;
	private MultiWordSuggestOracle addPlayerOracle = new MultiWordSuggestOracle();
	private Map<String, Player> allPlayers;
	private final SpelstegenServiceAsync spelstegenService;
	private GetPlayerCallback getPlayerCallback = new GetPlayerCallback();
	private GetPlayerStatusCallback getPlayerStatusCallback = new GetPlayerStatusCallback();
	private GetLeaguePlayersStatusCallback getLeaguePlayersStatusCallback = new GetLeaguePlayersStatusCallback();
	private AddAdminCallback addAdminCallback = new AddAdminCallback();
	private AlphabeticComparator alphabeticComparator = new AlphabeticComparator();
	private League league;
	private Player loggedInPlayer;
	private Button addPlayerButton;
	private Grid playerGrid;
	
	public LeagueAdminPanel(final SpelstegenServiceAsync spelstegenService, final LeagueUpdater leagueUpdater) {
		this.spelstegenService = spelstegenService;
		leagueUpdater.addLeagueUpdateListener(this);
		allPlayers = new HashMap<String, Player>();
		VerticalPanel mainPanel = MainApplication.createStandardVerticalPanel();

		
		playerGrid = new Grid(1 + 1, headers.length);
		for (int i = 0; i < headers.length; i++) {
			playerGrid.setText(0, i, headers[i]);
			playerGrid.getCellFormatter().setStyleName(0, i, "table-caption");
		}
		
		VerticalPanel playerTablePanel = MainApplication.createStandardVerticalPanel();
		playerTablePanel.add(new Label("Spelare i ligan"));
		playerTablePanel.add(playerGrid);
		
		// TODO Basic functionality for displaying the list of players, still working on the db side.
//		mainPanel.add(playerTablePanel);
		
		// Add a new player to league
		Label addPanelLabel = new Label("Lägg till spelare till ligan");
		addPanelLabel.setStylePrimaryName("leagueLabel");
		
		VerticalPanel addPanel = MainApplication.createStandardVerticalPanel();
		addPanel.add(addPanelLabel);
		
		HorizontalPanel playerAddPanel = MainApplication.createStandardHorizontalPanel();
		playerAddPanel.add(new Label("Skriv in spelarens epostadress."));
		   
		addPlayerBox = new SuggestBox(addPlayerOracle);
		addPlayerBox.addFocusListener(this);
		playerAddPanel.add(addPlayerBox);
		
		addPlayerButton = new Button("Lägg till");
		addPlayerButton.setEnabled(false);
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				Window.alert("Failed to add player to league: " + arg0.getMessage());
			}

			public void onSuccess(Void arg0) {
				MainApplication.showMessage("Spelaren tillagd.", false);
				leagueUpdater.updateLeague();
			}
		};
		
		addPlayerButton.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				spelstegenService.addPlayerToLeague(league.getId(), 
						allPlayers.get(addPlayerBox.getText().trim()).getId(), loggedInPlayer.getId(), callback);
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
	
	private class GetPlayerStatusCallback implements AsyncCallback<PlayerStatus> {
		public void onFailure(Throwable caught) {
			
		}

		public void onSuccess(PlayerStatus result) {
			if (result == PlayerStatus.LEAGUE_ADMIN || result == PlayerStatus.SUPER_USER) {
				addPlayerButton.setEnabled(true);
			}
		}
	}
	
	private class GetLeaguePlayersStatusCallback implements AsyncCallback<Map<Integer, PlayerStatus>> {
		public void onFailure(Throwable arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onSuccess(Map<Integer, PlayerStatus> result) {
			List<Player> players = league.getPlayers();
			List<PlayerAdmin> playerAdminCheckboxes = new ArrayList<PlayerAdmin>();
			for (Player player : players) {
				playerAdminCheckboxes.add(new PlayerAdmin(player.getId(), result.get(player.getId())));
			}
			Collections.sort(players, alphabeticComparator);
			playerGrid.resizeRows(players.size()+1);
			int col = 0;
			Player p = players.get(0);
			for (int i = 0; i < players.size(); i++) {
				playerGrid.setText(i+1, col++, p.getPlayerName() + "(" + p.getNickName() + ")");
				playerGrid.setText(i+1, col++, p.getEmail());
				playerGrid.setWidget(i+1, col++, playerAdminCheckboxes.get(i).getLeagueAdminBox());
				playerGrid.setWidget(i+1, col++, playerAdminCheckboxes.get(i).getMatchAdminBox());
			}
		}
		
	}
	
	private class PlayerAdmin implements ClickListener {
		private int playerId;
		private CheckBox leagueAdminBox = new CheckBox();
		private CheckBox matchAdminBox = new CheckBox();
		
		PlayerAdmin(int playerId, PlayerStatus playerStatus) {
			this.playerId = playerId;
			if (playerStatus == PlayerStatus.SUPER_USER || playerStatus == PlayerStatus.LEAGUE_ADMIN) {
				leagueAdminBox.setChecked(true);
			}
			if (playerStatus == PlayerStatus.SUPER_USER || playerStatus == PlayerStatus.MATCH_ADMIN) {
				matchAdminBox.setChecked(true);
			}
			leagueAdminBox.addClickListener(this);
			matchAdminBox.addClickListener(this);
		}
		
		public CheckBox getLeagueAdminBox() {
			return leagueAdminBox;
		}
		
		public CheckBox getMatchAdminBox() {
			return matchAdminBox;
		}

		public void onClick(Widget sender) {
			if (sender == leagueAdminBox) {
				spelstegenService.addLeageAdmin(league.getId(), playerId, loggedInPlayer.getId(), addAdminCallback);
			} else if (sender == matchAdminBox) {
				spelstegenService.addLeagueMatchAdmin(league.getId(), playerId, loggedInPlayer.getId(), addAdminCallback);
			}
		}
	}
	
	private class AddAdminCallback implements AsyncCallback<Boolean> {
		public void onFailure(Throwable arg0) {
			
		}

		public void onSuccess(Boolean arg0) {
			
		}
	}
	
	private class AlphabeticComparator implements Comparator<Player> {
		public int compare(Player o1, Player o2) {
			return o1.getPlayerName().toLowerCase().compareTo(o2.getPlayerName().toLowerCase());
		}
	};

	public void onFocus(Widget widget) {
		if (widget == addPlayerBox) {
			spelstegenService.getPlayers(getPlayerCallback);
		}
	}

	public void onLostFocus(Widget arg0) {
		// TODO Auto-generated method stub
		
	}

	public void leagueUpdated(League league) {
		this.league = league;
	}

	public void loggedIn(Player player) {
		loggedInPlayer = player;
	}

	public void loggedOut() {
		addPlayerButton.setEnabled(false);
		
	}
	
}
