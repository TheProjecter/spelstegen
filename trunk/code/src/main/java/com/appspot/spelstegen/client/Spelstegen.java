package com.appspot.spelstegen.client;

import java.util.List;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.LeagueUpdateListener;
import com.appspot.spelstegen.client.services.LoginListener;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.appspot.spelstegen.client.widgets.LoginPanel;
import com.appspot.spelstegen.client.widgets.OverviewPanel;
import com.appspot.spelstegen.client.widgets.PlayerPanel;
import com.appspot.spelstegen.client.widgets.SplashPanel;
import com.appspot.spelstegen.client.widgets.league.LeaguePanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point class for the gui.
 * 
 * @author Henrik Segesten
 */
public class Spelstegen implements EntryPoint, LeagueUpdateListener, LoginListener {

	private final static int MAINPANEL_WIDTH = 800;
	private final static int MAINPANEL_HEIGHT = 600;
	private final static int PADDING = 3;
	private final static int CHILDPANEL_WIDTH = MAINPANEL_WIDTH - (2 * PADDING);
	private final static int CHILDPANEL_HEIGHT = MAINPANEL_HEIGHT - (2 * PADDING);
	public final static int NUMBER_OF_COLUMNS = 3;
	public final static int HORISONTAL_SPACING = 5;
	public final static int VERTICAL_SPACING = 15;

	private GetLeaguesCallBack getLeaguesCallBack = new GetLeaguesCallBack();

	private static PopupPanel popup;
	private static SplashPanel splashPanel;
	private PushButton addPlayerButton;
	private PushButton loginButton;
	private PushButton changeProfileButton;
	private LoginClickHandler loginClickHandler;
	private League currentLeague;
	private Player loggedInPlayer;
	private List<League> loggedInPlayerLeagues;
	private VerticalPanel contentPanel;
	private LeaguePanel leaguePanel;
	private OverviewPanel overviewPanel;
	private String header = "Spelstegen - version ";
	private Label topLabel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		VerticalPanel mainPanel = createStandardVerticalPanel();
		mainPanel.setSpacing(0);
		mainPanel.setSize(String.valueOf(MAINPANEL_WIDTH) + "px", String.valueOf(MAINPANEL_HEIGHT) + "px");
		topLabel = new Label(header);
		topLabel.setStylePrimaryName("header");
		mainPanel.add(topLabel);
		contentPanel = createStandardVerticalPanel();
		leaguePanel = new LeaguePanel(CHILDPANEL_WIDTH, CHILDPANEL_HEIGHT);
		overviewPanel = new OverviewPanel();
		PushButton overviewButton = new PushButton("Välj liga");
		overviewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("");
				showOverview();
			}
		});
		loginButton = new PushButton("Logga in");
		loginClickHandler = new LoginClickHandler();
		loginButton.addClickHandler(loginClickHandler);
		addPlayerButton = new PushButton("Lägg till spelare");
		addPlayerButton.setEnabled(false);
		addPlayerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPlayerWindow(null);
				
			}
		});
		changeProfileButton = new PushButton("Ändra min profil");
		changeProfileButton.setEnabled(false);
		changeProfileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
		RootPanel.get("root").add(mainPanel);
		RootPanel.get().setStyleName("rootpanel");
		mainPanel.setStyleName("mainpanel");
		splashPanel = new SplashPanel();
		
		ServiceManager.getInstance().getVersionString(new AsyncCallback<String>() {
			public void onFailure(Throwable arg0) {
				topLabel.setText(header + "-1");
			}

			public void onSuccess(String version) {
				topLabel.setText(header + version);
			}
		});
		
		ServiceManager.getInstance().addLoginListener(this);
		ServiceManager.getInstance().addLeagueUpdateListener(this);

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
		final PlayerPanel playerPanel = new PlayerPanel(player, currentLeague);
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
		panel.setSpacing(Spelstegen.HORISONTAL_SPACING);
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
			} else {
				Window.alert("Error:" + loggedInPlayer + " does not belong to any league.");
			}
		}
	}

	private class LoginClickHandler implements ClickHandler {

		private boolean loggedIn = false;

		public void setLoggedIn(boolean loggedIn) {
			this.loggedIn = loggedIn;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (!loggedIn) {
				final LoginPanel loginPanel = new LoginPanel();
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

	public void fetchPlayerLeagues() {
		ServiceManager.getInstance().getLeagues(loggedInPlayer, getLeaguesCallBack);
	}

	@Override
	public void loggedIn(Player player) {
		this.loggedInPlayer = player;
		addPlayerButton.setEnabled(true);
		loginButton.setText("Logga ut");
		loginClickHandler.setLoggedIn(true);
		changeProfileButton.setEnabled(true);
		showStatus("Inloggad: " + player.getPlayerName());
		fetchPlayerLeagues();
	}

	@Override
	public void loggedOut() {
		addPlayerButton.setEnabled(false);
		changeProfileButton.setEnabled(false);
		loginButton.setText("Logga in");
		showStatus("Utloggad");
	}
	
	@Override
	public void leagueUpdated(League league) {
		currentLeague = league;
		showLeagueView();		
	}

}
