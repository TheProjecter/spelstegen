package com.appspot.spelstegen.client.widgets.league;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Set;
import com.appspot.spelstegen.client.entities.Sport;
import com.appspot.spelstegen.client.services.LeagueUpdateListener;
import com.appspot.spelstegen.client.services.LoginListener;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class that draws the register result panel.
 * 
 * @author Henrik Segesten
 * 
 */
public class RegisterResultPanel extends Composite implements LeagueUpdateListener, LoginListener {

	private List<TextBox> player1Score;
	private List<TextBox> player2Score;
	private VerticalPanel scorePanel;
	private RadioButton oneSet;
	private RadioButton threeSet;
	private RadioButton fiveSet;
	private int setMode = 1;
	private League league;
	private ListBox player1Box;
	private ListBox player2Box;
	private ListBox sportBox;
	private Player loggedInPlayer;
	private VerticalPanel mainPanel;
	private boolean panelIsVisible;
	private PushButton saveButton;

	public RegisterResultPanel() {
		ServiceManager.getInstance().addLeagueUpdateListener(this);
		ServiceManager.getInstance().addLoginListener(this);
		
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(Spelstegen.VERTICAL_SPACING);
		panelIsVisible = false;
		initWidget(mainPanel);
	}

	private void setScoreBoxes(int sets) {
		player1Score.clear();
		player2Score.clear();
		scorePanel.clear();
		for (int i = 0; i < sets; i++) {
			scorePanel.add(createScoreRow());
		}
		setMode = sets;
	}

	private HorizontalPanel createScoreRow() {
		HorizontalPanel scorePanel = Spelstegen.createStandardHorizontalPanel();
		TextBox p1Score = new TextBox();
		player1Score.add(p1Score);
		scorePanel.add(p1Score);
		scorePanel.add(new Label(" - "));
		TextBox p2Score = new TextBox();
		player2Score.add(p2Score);
		scorePanel.add(p2Score);
		return scorePanel;
	}

	private class SetClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if (sender == oneSet && setMode != 1) {
				setScoreBoxes(1);
			} else if (sender == threeSet && setMode != 3) {
				setScoreBoxes(3);
			} else if (sender == fiveSet && setMode != 5) {
				setScoreBoxes(5);
			}			
		}
	}

	private void populatePlayerBox(ListBox playerBox) {
		playerBox.clear();
		playerBox.addItem("Välj en spelare");
		for (Player player : league.getPlayers()) {
			playerBox.addItem(player.getPlayerName());
		}
	}

	private void submitMatch() {
		Match m = new Match(league.getId(), new Date(), league.getSports().get(sportBox.getSelectedIndex()), 
				league.getPlayers().get(player1Box.getSelectedIndex()-1), 
				league.getPlayers().get(player2Box.getSelectedIndex()-1));
		for (int i = 0; i < player1Score.size(); i++) {
			try {
				if (!player1Score.get(i).getText().equals("") && !player2Score.get(i).getText().equals("")) {
					m.addSet(new Set(m.getSport(), Integer.parseInt(player1Score.get(i).getText()), 
							Integer.parseInt(player2Score.get(i).getText())));
				}
			} catch (NumberFormatException e) {
				Window.alert("Failed to parse number: " + e.getMessage());
			}
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to save match.");
			}

			public void onSuccess(Void arg) {
				Spelstegen.showMessage("Match sparad.", false);
				resetPanel();
			}
		};
		ServiceManager.getInstance().saveMatch(m, loggedInPlayer, league, callback);
	}

	public void leagueUpdated(League league) {
		this.league = league;
		updatePanel();
	}

	public void loggedIn(Player player) {
		this.loggedInPlayer = player;
		updatePanel();
	}

	public void loggedOut() {
		loggedInPlayer = null;
		updatePanel();
	}
		
	private void updatePanel() {
		if ((loggedInPlayer == null) || (league == null) || (!league.getPlayers().contains(loggedInPlayer))) {
			mainPanel.clear();
			panelIsVisible = false;
			return;
		} else if (!panelIsVisible) {
			populatePanel();
			panelIsVisible = true;
		}
		populatePlayerBox(player1Box);
		populatePlayerBox(player2Box);
	}
	
	private void populatePanel() {
		player1Box = new ListBox(false);
		populatePlayerBox(player1Box);
		Label vsLabel = new Label(" spelade mot ");
		player2Box = new ListBox(false);
		populatePlayerBox(player2Box);
		HorizontalPanel playerPanel = Spelstegen.createStandardHorizontalPanel();
		playerPanel.add(player1Box);
		playerPanel.add(vsLabel);
		playerPanel.add(player2Box);
		
		
		sportBox = new ListBox(false);
		if (league.getSports().size() > 1) {
			sportBox.addItem("Välj sport");
			for (Sport sport : league.getSports()) {
				sportBox.addItem(sport.getName());
			}
		} else {
			sportBox.addItem(league.getSports().get(0).getName());
			sportBox.setEnabled(false);
		}
		
		HorizontalPanel sportPanel = Spelstegen.createStandardHorizontalPanel();
		sportPanel.add(new Label("i:"));
		sportPanel.add(sportBox);

		Label setLabel = new Label("Spel om bäst av");
		oneSet = new RadioButton("setSelection", "1");
		threeSet = new RadioButton("setSelection", "3");
		fiveSet = new RadioButton("setSelection", "5");
		oneSet.setValue(true);
		SetClickHandler setClickHandler = new SetClickHandler();
		oneSet.addClickHandler(setClickHandler);
		threeSet.addClickHandler(setClickHandler);
		fiveSet.addClickHandler(setClickHandler);
		Label setLabel2 = new Label(" set");
		HorizontalPanel setPanel = Spelstegen.createStandardHorizontalPanel();
		setPanel.add(setLabel);
		setPanel.add(oneSet);
		setPanel.add(threeSet);
		setPanel.add(fiveSet);
		setPanel.add(setLabel2);

		player1Score = new ArrayList<TextBox>(5);
		player2Score = new ArrayList<TextBox>(5);
		scorePanel = new VerticalPanel();
		scorePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setScoreBoxes(1);

		saveButton = new PushButton("Spara");
		if (loggedInPlayer != null) {
			saveButton.setEnabled( loggedInPlayer.isLeagueAdmin(league.getId()) );
		}
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				submitMatch();
			}
		});

		HorizontalPanel buttonPanel = Spelstegen.createStandardHorizontalPanel();
		buttonPanel.add(saveButton);
		mainPanel.add(playerPanel);
		mainPanel.add(sportPanel);
		mainPanel.add(setPanel);
		mainPanel.add(scorePanel);
		mainPanel.add(buttonPanel);
		
	}
	
	private void resetPanel() {
		player1Box.setSelectedIndex(0);
		player2Box.setSelectedIndex(0);
		sportBox.setSelectedIndex(0);
		oneSet.setValue(true);
		setScoreBoxes(1);
	}

}
