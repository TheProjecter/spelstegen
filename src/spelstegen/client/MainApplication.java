package spelstegen.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spelstegen.client.widgets.LoginPanel;
import spelstegen.client.widgets.PlayerPanel;
import spelstegen.client.widgets.RegisterResultPanel;
import spelstegen.client.widgets.StatisticsPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point class for the gui.
 * 
 * @author Henrik Segesten
 */
public class MainApplication implements EntryPoint {

	public final static int NUMBER_OF_COLUMNS = 3;
	public final static int HORISONTAL_SPACING = 5;
	public final static int VERTICAL_SPACING = 15;
	private Grid mainTable;
	private Grid matchTable;
	private StatisticsPanel statisticsPanel = new StatisticsPanel();
	private ToggleButton tableButton = new ToggleButton("Tabell");
	private ToggleButton matchesButton = new ToggleButton("Matcher");
	private ToggleButton statisticsButton = new ToggleButton("Statistik");
	private static PopupPanel popup;
	private static SpelstegenServiceAsync spelstegenService;
	static Map<String,Player> players;
	private List<Match> matches;
	private List<Player> playerList;
	private GetPlayersCallBack getPlayersCallBack = new GetPlayersCallBack();
	private GetMatchesCallback getMatchesCallback = new GetMatchesCallback();
	private LadderCalculator ladderCalculator = new LadderCalculator();
	private PushButton inputMatchButton;
	private PushButton addPlayerButton;
	private PushButton loginButton;
	private LoginClickListener loginClickListener;
	
	private VerticalPanel contentPanel = new VerticalPanel();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Init RPC service
		spelstegenService = (SpelstegenServiceAsync) GWT.create(SpelstegenService.class);
		ServiceDefTarget enpoint = (ServiceDefTarget) spelstegenService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "spelstegenService";
		enpoint.setServiceEntryPoint(moduleRelativeURL);
		
		// Init
		players = new HashMap<String, Player>();
		playerList = new ArrayList<Player>();
		matches = new ArrayList<Match>();
		updatePlayerList();
		updateMatchList();
		mainTable = new Grid(players.size() > 0 ? players.size() : 3, NUMBER_OF_COLUMNS);
		matchTable = new Grid(3, 4);
		
		
		// Construct gui
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setSpacing(VERTICAL_SPACING);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label topLabel = new Label("Epsilons squashstege", false);
		topLabel.setStylePrimaryName("toplabel");
		mainPanel.add(topLabel);
		
		// Top buttons
		tableButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (tableButton.isDown()) {
					matchesButton.setDown(false);
					statisticsButton.setDown(false);
					showTableView();
				}
			}
		});
		matchesButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (matchesButton.isDown()) {
					tableButton.setDown(false);
					statisticsButton.setDown(false);
					showMatchView();
				}
			}
		});
		statisticsButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (statisticsButton.isDown()) {
					tableButton.setDown(false);
					matchesButton.setDown(false);
					showStatisticsView();
				}
			}
		});	
		tableButton.setDown(true);
		HorizontalPanel topButtonPanel = new HorizontalPanel();
		topButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topButtonPanel.setSpacing(HORISONTAL_SPACING);
		topButtonPanel.add(tableButton);
		topButtonPanel.add(matchesButton);
		topButtonPanel.add(statisticsButton);
		mainPanel.add(topButtonPanel);
		
		contentPanel.setSpacing(VERTICAL_SPACING);
		contentPanel.setStylePrimaryName("outer-border");
		contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.add(contentPanel);
		
		// Bottom buttons
		inputMatchButton = new PushButton("Registrera match");
		inputMatchButton.setEnabled(false);
		inputMatchButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final RegisterResultPanel registerResultPanel = new RegisterResultPanel(spelstegenService, playerList, MainApplication.this);
				registerResultPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            registerResultPanel.setPopupPosition(left + 100, top);
			          }
				});
			}
		});
		loginButton = new PushButton("Logga in");
		loginClickListener = new LoginClickListener();
		loginButton.addClickListener(loginClickListener);
		addPlayerButton = new PushButton("Lägg till spelare");
		addPlayerButton.setEnabled(false);
		addPlayerButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final PlayerPanel playerPanel = new PlayerPanel(spelstegenService, MainApplication.this);
				playerPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            playerPanel.setPopupPosition(left + 100, top);
			          }
				});
			}
		});
		
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomButtonPanel.setSpacing(HORISONTAL_SPACING);
		bottomButtonPanel.add(inputMatchButton);
		bottomButtonPanel.add(loginButton);
		bottomButtonPanel.add(addPlayerButton);
		
		mainPanel.add(bottomButtonPanel);
		
		showTableView();
		RootPanel.get().add(mainPanel);
		

	}
	
	private void showTableView() {
		contentPanel.clear();
		
		mainTable.setCellSpacing(0);
		mainTable.setWidth("600px");
		mainTable.setText(0, 0, "#");
		mainTable.setText(0, 1, "Spelare");
		mainTable.setText(0, 2, "Poäng");
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(0, i, "table-caption"); 
		}
		
		contentPanel.add(mainTable);
	}
	
	private void showMatchView() {
		contentPanel.clear();
		updateMatchList();
		matchTable.setCellSpacing(0);
		matchTable.setWidth("600px");
		contentPanel.add(matchTable);
	}
	
	public void showStatisticsView() {
		contentPanel.clear();
		contentPanel.add(statisticsPanel);
	}

	public void populateMatches() {
		/*matchTable.resize(matches.size(), 4);
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(i);
			matchTable.setText(i, 0, match.getDate().toString());
			Player p = getPlayer(match.getWinner());
			matchTable.setHTML(i, 1, "<b>" + (p != null ? p.getPlayerName() : match.getWinner()) + "</b>");
			p = getPlayer(match.getLoser());
			matchTable.setText(i, 2, (p != null ? p.getPlayerName() : match.getLoser()));
			matchTable.setText(i, 3, match.getScores(true));
		}*/
	}
	
	public void populateTable() {
		int col = 0;
		Player p = null;
		mainTable.resize(playerList.size()+1, NUMBER_OF_COLUMNS);
		for (int i = 0; i < playerList.size(); i++) {
			p = playerList.get(i);
			mainTable.setText(i+1, col++, i+1 + "");
			mainTable.setText(i+1, col++, p.getPlayerName());
			mainTable.setText(i+1, col++, p.getPoints() + "");
			col = 0;
		}
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(playerList.size(), i, "table-caption");
		}
	}
	
	public static HorizontalPanel createHorizontalPanel() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(MainApplication.HORISONTAL_SPACING);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		return panel;
	}
	
    public static void showMessage(String text, boolean isError) {
    	popup = new PopupPanel(true);
    	popup.setAnimationEnabled(true);
    	popup.setWidget(new HTML(text));
    	popup.show();
    }
    
    public static void showStatus(String text) {
    	popup = new PopupPanel(false);
    	popup.setAnimationEnabled(true);
    	popup.setWidget(new HTML(text));
    	popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
	            int left = (Window.getClientWidth() - offsetWidth);
	            popup.setPopupPosition(left, 0);
	          }
		});
    }
    
    public static Player getPlayer(String email) {
    	return players.get(email);
    }
    
    public void updatePlayerList() {
		spelstegenService.getPlayers(getPlayersCallBack);
    }
    
    public void updateMatchList() {
    	spelstegenService.getMatches(-1, getMatchesCallback);
    }
    
    public void loggedIn() {
    	addPlayerButton.setEnabled(true);
    	inputMatchButton.setEnabled(true);
    	loginButton.setText("Logga ut");
    	loginClickListener.setLoggedIn(true);
    }
    
    private class GetMatchesCallback implements AsyncCallback<List<Match>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to get matches. " + caught.getMessage());
		}

		public void onSuccess(List<Match> result) {
			matches = result;
			populateMatches();
			ladderCalculator.reset();
			ladderCalculator.addMatches(result);
			Collections.sort(playerList);
			populateTable();
		}
    	
    }
    
    private class GetPlayersCallBack implements AsyncCallback<List<Player>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to get players. " + caught.getMessage());
		}

		public void onSuccess(List<Player> result) {
			players.clear();
			for (Player player : result) {
				players.put(player.getEmail(), player);
			}
			playerList = result;
			Collections.sort(playerList);
			populateTable();
		}
    }

    private class LoginClickListener implements ClickListener {
    	
    	private boolean loggedIn = false;
    	
    	public LoginClickListener() {
    	}
    	
    	public void setLoggedIn(boolean loggedIn) {
    		this.loggedIn = loggedIn;
    	}
    	
    	
    	public void onClick(Widget sender) {
    		if (!loggedIn) {
    			final LoginPanel loginPanel = new LoginPanel(spelstegenService, MainApplication.this);
    			loginPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
    				public void setPosition(int offsetWidth, int offsetHeight) {
    					int left = (Window.getClientWidth() - offsetWidth) / 3;
    					int top = (Window.getClientHeight() - offsetHeight) / 3;
    					loginPanel.setPopupPosition(left + 100, top);
    				}
    			});
    		} else {
    			addPlayerButton.setEnabled(false);
    			inputMatchButton.setEnabled(false);
    			loggedIn = false;
    			loginButton.setText("Logga in");
    		}
		}
    }
}
