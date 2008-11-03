package spelstegen.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spelstegen.client.Match.MatchDoneException;
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
	private static Map<String,Player> players;
	private GetPlayersCallBack getPlayersCallBack = new GetPlayersCallBack();
	
	private VerticalPanel contentPanel = new VerticalPanel();
	
	
	// Test data
	private List<Match> matchData = new ArrayList<Match>();
	
	{
		Player p1 = new Player("Åke", "ake@ake.se");
		p1.changePoints(53);
		Player p2 = new Player("Elsa", "elsa@elsa.se");
		p2.changePoints(-34);
		Match m1 = new Match("HT08", new Date(), p1.getEmail(), p2.getEmail());
		Match m2 = new Match("HT08", new Date(), p1.getEmail(), p2.getEmail());
		try {
			m1.addSet(15, 1);
			m2.addSet(15, 1);
			m2.addSet(4, 15);
			m2.addSet(6, 15);
		} catch (MatchDoneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		matchData.add(m1);
		matchData.add(m2);
	}
	
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
		updatePlayerList();
		
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
		PushButton inputMatchButton = new PushButton("Registrera match");
		inputMatchButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final RegisterResultPanel registerResultPanel = new RegisterResultPanel(players.values());
				registerResultPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            registerResultPanel.setPopupPosition(left + 100, top);
			          }
				});
			}
		});
		PushButton loginButton = new PushButton("Logga in");
		loginButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final LoginPanel loginPanel = new LoginPanel(spelstegenService);
				loginPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            loginPanel.setPopupPosition(left + 100, top);
			          }
				});
			}
		});
		PushButton addPlayerButton = new PushButton("Lägg till spelare");
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
		
		mainTable = new Grid(3, NUMBER_OF_COLUMNS);
		mainTable.setCellSpacing(0);
		mainTable.setWidth("600px");
		mainTable.setText(0, 0, "#");
		mainTable.setText(0, 1, "Spelare");
		mainTable.setText(0, 2, "Poäng");
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(0, i, "table-caption"); 
		}
		
		contentPanel.add(mainTable);
		populateTable();
	}
	
	private void showMatchView() {
		contentPanel.clear();
		populateMatches(matchData);
		contentPanel.add(matchTable);
	}
	
	private void showStatisticsView() {
		contentPanel.clear();
		contentPanel.add(statisticsPanel);
	}
	
	private void populateMatches(List<Match> matches) {
		matchTable = new Grid(matches.size(), 4);
		matchTable.setCellSpacing(0);
		matchTable.setWidth("600px");
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(i);
			matchTable.setText(i, 0, match.getDate().toString());
			Player p = getPlayer(match.getWinner());
			matchTable.setHTML(i, 1, "<b>" + (p != null ? p.getPlayerName() : match.getWinner()) + "</b>");
			p = getPlayer(match.getLoser());
			matchTable.setText(i, 2, (p != null ? p.getPlayerName() : match.getLoser()));
			matchTable.setText(i, 3, match.getScores(true));
		}
	}
	
	public void populateTable() {
		int col = 0;
		List<Player> playersList = new ArrayList<Player>(players.size());
		for (Player player : players.values()) {
			playersList.add(player);
		}
		Collections.sort(playersList);
		Player p = null;
		for (int i = 0; i < playersList.size(); i++) {
			p = playersList.get(i);
			mainTable.setText(i+1, col++, i+1 + "");
			mainTable.setText(i+1, col++, p.getPlayerName());
			mainTable.setText(i+1, col++, p.getPoints() + "");
			col = 0;
		}
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(playersList.size(), i, "table-caption");
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
    	if (!isError) {
    		popup.setStylePrimaryName("popup");
    	} else {
    		popup.setStylePrimaryName("popupError");
    	}
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
    
    private class GetPlayersCallBack implements AsyncCallback<List<Player>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to get players. " + caught.getMessage());
		}

		public void onSuccess(List<Player> result) {
			players.clear();
			for (Player player : result) {
				players.put(player.getEmail(), player);
			}
			MainApplication.this.populateTable();
		}
    	
    }

}
