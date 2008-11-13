package spelstegen.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spelstegen.client.widgets.LoginPanel;
import spelstegen.client.widgets.MainLeaguePanel;
import spelstegen.client.widgets.MatchesTable;
import spelstegen.client.widgets.PlayerPanel;
import spelstegen.client.widgets.RegisterResultPanel;
import spelstegen.client.widgets.StatisticsPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point class for the gui.
 * 
 * @author Henrik Segesten
 */
public class MainApplication implements EntryPoint, LeagueUpdater {

	public final static int NUMBER_OF_COLUMNS = 3;
	public final static int HORISONTAL_SPACING = 5;
	public final static int VERTICAL_SPACING = 15;
	
	
	private static SpelstegenServiceAsync spelstegenService;
	private GetLeaguesCallBack getLeaguesCallBack = new GetLeaguesCallBack();
	private VerticalPanel contentPanel = new VerticalPanel();
	private TabPanel tabPanel = new TabPanel();
	private StatisticsPanel statisticsPanel;
	private MainLeaguePanel mainLeaguePanel;
	private MatchesTable matchesTable;
	private static PopupPanel popup;
	private PushButton inputMatchButton;
	private PushButton addPlayerButton;
	private PushButton loginButton;
	private PushButton changeProfileButton;
	private Label leagueNameLabel;
	private LoginClickListener loginClickListener;
	private League currentLeague;
	private List<League> allPlayerLeagues;
	private Player loggedInPlayer;

	private List<LeagueUpdateListener> leagueUpdateListeners = new ArrayList<LeagueUpdateListener>();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Init RPC service
		spelstegenService = (SpelstegenServiceAsync) GWT.create(SpelstegenService.class);
		ServiceDefTarget enpoint = (ServiceDefTarget) spelstegenService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "spelstegenService";
		enpoint.setServiceEntryPoint(moduleRelativeURL);
		
		// Construct gui
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setSpacing(VERTICAL_SPACING);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		leagueNameLabel = new Label("", false);
		leagueNameLabel.setStylePrimaryName("toplabel");
		mainPanel.add(leagueNameLabel);
		
		// Tab panel
		statisticsPanel = new StatisticsPanel(this);
		mainLeaguePanel = new MainLeaguePanel(this);
		matchesTable = new MatchesTable(spelstegenService, this);
		tabPanel.add(mainLeaguePanel, "Tabell");
		tabPanel.add(matchesTable, "Matcher");
		tabPanel.add(statisticsPanel, "Statistik");
		tabPanel.selectTab(0);
		mainPanel.add(tabPanel);
		
		contentPanel.setSpacing(VERTICAL_SPACING);
		contentPanel.setStylePrimaryName("outer-border");
		contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.add(contentPanel);
		
		// Bottom buttons
		inputMatchButton = new PushButton("Registrera match");
		inputMatchButton.setEnabled(false);
		inputMatchButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final RegisterResultPanel registerResultPanel = new RegisterResultPanel(spelstegenService, MainApplication.this);
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
				showPlayerWindow(null);
			}
		});
		
		changeProfileButton = new PushButton("Ändra min profil");
		changeProfileButton.setEnabled(false);
		changeProfileButton.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				showPlayerWindow(loggedInPlayer);
			}
		});
		
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomButtonPanel.setSpacing(HORISONTAL_SPACING);
		bottomButtonPanel.add(loginButton);
		bottomButtonPanel.add(inputMatchButton);
		bottomButtonPanel.add(changeProfileButton);
		bottomButtonPanel.add(addPlayerButton);
		
		mainPanel.add(bottomButtonPanel);
		RootPanel.get().add(mainPanel);
		
		updateLeague();
	}
	
	private void showPlayerWindow(Player player) {
		final PlayerPanel playerPanel = new PlayerPanel(spelstegenService, MainApplication.this, player);
		playerPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
	            int left = (Window.getClientWidth() - offsetWidth) / 3;
	            int top = (Window.getClientHeight() - offsetHeight) / 3;
	            playerPanel.setPopupPosition(left + 100, top);
	          }
		});
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
    
    public void loggedIn(Player player) {
    	this.loggedInPlayer = player;
    	addPlayerButton.setEnabled(true);
    	inputMatchButton.setEnabled(true);
    	loginButton.setText("Logga ut");
    	loginClickListener.setLoggedIn(true);
    	changeProfileButton.setEnabled(true);
    	updateLeague();
    }
    
    private class GetLeaguesCallBack implements AsyncCallback<List<League>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to get players. " + caught.getMessage());
		}

		public void onSuccess(List<League> result) {
			allPlayerLeagues = result;
			currentLeague = allPlayerLeagues.get(0);
			leagueNameLabel.setText(currentLeague.getName());
			Collections.sort(currentLeague.getPlayers());
			notifyLeagueUpdated();
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
    			changeProfileButton.setEnabled(false);
    			loggedIn = false;
    			loginButton.setText("Logga in");
    		}
		}
    }
    
    private void notifyLeagueUpdated() {
    	for (LeagueUpdateListener listener : leagueUpdateListeners) {
			listener.leagueUpdated(currentLeague);
		}
    }

	public void addLeagueUpdateListener(LeagueUpdateListener listener) {
		leagueUpdateListeners.add(listener);
		
	}

	public void updateLeague() {
		
		if (loggedInPlayer == null) {
			//TODO: Remove this code
			loggedInPlayer = new Player();
			loggedInPlayer.setId(1);
		}
		spelstegenService.getLeagues(loggedInPlayer, getLeaguesCallBack);
	}

	
}
