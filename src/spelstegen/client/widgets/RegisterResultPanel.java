package spelstegen.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import spelstegen.client.League;
import spelstegen.client.LeagueUpdater;
import spelstegen.client.MainApplication;
import spelstegen.client.Match;
import spelstegen.client.Player;
import spelstegen.client.Set;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.Sport;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class that draws the register result panel.
 * @author Henrik Segesten
 *
 */
public class RegisterResultPanel extends PopupPanel {

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
	private SpelstegenServiceAsync spelstegenService;
	final LeagueUpdater leagueUpdater;

	public RegisterResultPanel(SpelstegenServiceAsync spelstegenService, League league, LeagueUpdater leagueUpdater) {
		super(false);
		this.spelstegenService = spelstegenService;
		this.league = league;
		this.leagueUpdater = leagueUpdater;
		
		player1Box = new ListBox(false);
		populatePlayerBox(player1Box);
		Label vsLabel = new Label(" spelade mot ");
		player2Box = new ListBox(false);
		populatePlayerBox(player2Box);
		HorizontalPanel playerPanel = MainApplication.createStandardHorizontalPanel();
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
		
		HorizontalPanel sportPanel = MainApplication.createStandardHorizontalPanel();
		sportPanel.add(new Label("i:"));
		sportPanel.add(sportBox);

		Label setLabel = new Label("Spel om bäst av");
		oneSet = new RadioButton("setSelection", "1");
		threeSet = new RadioButton("setSelection", "3");
		fiveSet = new RadioButton("setSelection", "5");
		oneSet.setChecked(true);
		SetClickListener setClickListener = new SetClickListener();
		oneSet.addClickListener(setClickListener);
		threeSet.addClickListener(setClickListener);
		fiveSet.addClickListener(setClickListener);
		Label setLabel2 = new Label(" set");
		HorizontalPanel setPanel = MainApplication.createStandardHorizontalPanel();
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

		PushButton saveButton = new PushButton("Spara");
		saveButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				submitMatch();
			}
		});
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				RegisterResultPanel.this.hide();
			}
		});
		HorizontalPanel buttonPanel = MainApplication.createStandardHorizontalPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		mainPanel.add(new Label("Registrera ny match"));
		mainPanel.add(playerPanel);
		mainPanel.add(sportPanel);
		mainPanel.add(setPanel);
		mainPanel.add(scorePanel);
		mainPanel.add(buttonPanel);
		this.add(mainPanel);
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
		HorizontalPanel scorePanel = MainApplication.createStandardHorizontalPanel();
		TextBox p1Score = new TextBox();
		player1Score.add(p1Score);
		scorePanel.add(p1Score);
		scorePanel.add(new Label(" - "));
		TextBox p2Score = new TextBox();
		player2Score.add(p2Score);
		scorePanel.add(p2Score);
		return scorePanel;
	}

	private class SetClickListener implements ClickListener {

		public void onClick(Widget sender) {
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
		playerBox.addItem("Välj en spelare");
		for (Player player : league.getPlayers()) {
			playerBox.addItem(player.getPlayerName());
		}
	}

	private void submitMatch() {
		Match m = new Match(new Date(), league.getSports().get(sportBox.getSelectedIndex()), 
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
				Window.alert("Failed to save match. " + caught.getMessage());
			}

			public void onSuccess(Void result) {
				MainApplication.showMessage("Sparade match.", false);
				RegisterResultPanel.this.hide();
				leagueUpdater.updateLeague();
			}
		};
		spelstegenService.addMatch(m, league.getId(), callback);
	}

}
