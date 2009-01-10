package spelstegen.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spelstegen.client.entities.League;
import spelstegen.client.entities.Player;
import spelstegen.client.widgets.LeaguePanel;
import spelstegen.client.widgets.LoginPanel;
import spelstegen.client.widgets.OverviewPanel;
import spelstegen.client.widgets.PlayerPanel;
import spelstegen.client.widgets.SplashPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point class for the gui.
 * 
 * @author Henrik Segesten
 */
public class MainApplication implements EntryPoint, LeagueUpdater, LoginHandler, LeagueChanger, LoginListener {

	private final static int MAINPANEL_WIDTH = 800;
	private final static int MAINPANEL_HEIGHT = 600;
	private final static int PADDING = 3;
	private final static int CHILDPANEL_WIDTH = MAINPANEL_WIDTH - (2 * PADDING);
	private final static int CHILDPANEL_HEIGHT = MAINPANEL_HEIGHT - (2 * PADDING);
	public final static int NUMBER_OF_COLUMNS = 3;
	public final static int HORISONTAL_SPACING = 5;
	public final static int VERTICAL_SPACING = 15;

	private static SpelstegenServiceAsync spelstegenService;
	private GetLeaguesCallBack getLeaguesCallBack = new GetLeaguesCallBack();
	private GetLeagueCallback getLeagueCallback = new GetLeagueCallback();

	private static PopupPanel popup;
	private static SplashPanel splashPanel;
	private PushButton addPlayerButton;
	private PushButton loginButton;
	private PushButton changeProfileButton;
	private LoginClickListener loginClickListener;
	private League currentLeague;
	private Player loggedInPlayer;
	private List<League> loggedInPlayerLeagues;
	private VerticalPanel contentPanel;
	private LeaguePanel leaguePanel;
	private OverviewPanel overviewPanel;

	private List<LeagueUpdateListener> leagueUpdateListeners = new ArrayList<LeagueUpdateListener>();
	private List<LoginListener> loginListeners = new ArrayList<LoginListener>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Init RPC service
		spelstegenService = (SpelstegenServiceAsync) GWT.create(SpelstegenService.class);
		ServiceDefTarget enpoint = (ServiceDefTarget) spelstegenService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "spelstegenService";
		enpoint.setServiceEntryPoint(moduleRelativeURL);

		VerticalPanel mainPanel = createStandardVerticalPanel();
		mainPanel.setSpacing(0);
		mainPanel.setSize(String.valueOf(MAINPANEL_WIDTH) + "px", String.valueOf(MAINPANEL_HEIGHT) + "px");

		Label topLabel = new Label("Spelstegen - version 0.1");
		topLabel.setStylePrimaryName("header");
		mainPanel.add(topLabel);

		contentPanel = createStandardVerticalPanel();

		leaguePanel = new LeaguePanel(this, spelstegenService, CHILDPANEL_WIDTH, CHILDPANEL_HEIGHT);
		overviewPanel = new OverviewPanel(spelstegenService, this);

		PushButton overviewButton = new PushButton("Välj liga");
		overviewButton.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				showOverview();
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

		HorizontalPanel mainButtonPanel = createStandardHorizontalPanel();
		mainButtonPanel.add(overviewButton);
		mainButtonPanel.add(loginButton);
		mainButtonPanel.add(changeProfileButton);
		mainButtonPanel.add(addPlayerButton);

		//mainPanel.setBorderWidth(1);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(mainButtonPanel);
		mainPanel.add(contentPanel);
		RootPanel.get().add(mainPanel);
		RootPanel.get().setStyleName("rootpanel");
		mainPanel.setStyleName("mainpanel");
		splashPanel = new SplashPanel();



		showOverview();
	}

	private void showLeagueView() {
		contentPanel.clear();
		contentPanel.add(leaguePanel);
	}

	private void showOverview() {
		contentPanel.clear();
		contentPanel.add(overviewPanel);
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


	public static HorizontalPanel createStandardHorizontalPanel() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(MainApplication.HORISONTAL_SPACING);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		return panel;
	}

	public static VerticalPanel createStandardVerticalPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(VERTICAL_SPACING);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		return panel;
	}

	public static void showMessage(String text, boolean isError) {
		splashPanel.show(text, 2000);
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
		loginButton.setText("Logga ut");
		loginClickListener.setLoggedIn(true);
		changeProfileButton.setEnabled(true);
		for (LoginListener listener : loginListeners) {
			if (listener != this) {
				listener.loggedIn(player);
			}
		}
		showStatus("Inloggad: " + player.getPlayerName());
		fetchPlayerLeagues();
	}

	public void loggedOut() {
		addPlayerButton.setEnabled(false);
		changeProfileButton.setEnabled(false);
		loginButton.setText("Logga in");
		for (LoginListener listener : loginListeners) {
			if (listener != this) {
				listener.loggedOut();
			}
		}
		showStatus("Utloggad");
	}

	private class GetLeaguesCallBack implements AsyncCallback<List<League>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to leagues for player. " + caught.getMessage());
		}

		public void onSuccess(List<League> result) {
			loggedInPlayerLeagues = result;
			if ((result != null) && (!result.isEmpty())) {
				if (currentLeague == null) {
					currentLeague = loggedInPlayerLeagues.get(0);
				}
				notifyLeagueUpdated();
			} else {
				Window.alert("Error:" + loggedInPlayer + " does not belong to any league.");
			}
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
						loginPanel.center();

					}
				});
				// Set focus on the widget. We have to use a deferred command
				// since GWT will lose it again if we set it in-line here
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						loginPanel.setUserNameFocus(true);
					}
				});

			} else {
				loggedIn = false;
				loggedOut();
			}
		}
	}

	private void notifyLeagueUpdated() {
		if (currentLeague != null) {
			Collections.sort(currentLeague.getPlayers());
			for (LeagueUpdateListener listener : leagueUpdateListeners) {
				listener.leagueUpdated(currentLeague);
			}
		}
	}

	public void addLeagueUpdateListener(LeagueUpdateListener listener) {
		leagueUpdateListeners.add(listener);

	}

	public void fetchPlayerLeagues() {
		spelstegenService.getLeagues(loggedInPlayer, getLeaguesCallBack);
	}

	public void updateLeague() {
		changeToLeague(currentLeague.getId());
	}

	public void addLoginListener(LoginListener listener) {
		loginListeners.add(listener);
	}

	public void changeToLeague(int leagueId) {
		spelstegenService.getLeague(leagueId, getLeagueCallback);
	}

	private class GetLeagueCallback implements AsyncCallback<League> {

		public void onFailure(Throwable arg0) {
			Window.alert("Failed to get league: " + arg0.getMessage());
		}

		public void onSuccess(League league) {
			currentLeague = league;
			notifyLeagueUpdated();
			showLeagueView();

		}

	}

}
