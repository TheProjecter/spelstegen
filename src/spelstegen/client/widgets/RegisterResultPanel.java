package spelstegen.client.widgets;

import java.util.ArrayList;
import java.util.List;

import spelstegen.client.MainApplication;
import spelstegen.client.Player;

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
	private List<Player> players;
	
	public RegisterResultPanel(List<Player> players) {
		super(false);
		this.players = players;
		
		ListBox player1Box = new ListBox(false);
		populatePlayerBox(player1Box);
		Label vsLabel = new Label(" spelade mot ");
		ListBox player2Box = new ListBox(false);
		populatePlayerBox(player2Box);
		HorizontalPanel playerPanel = MainApplication.createHorizontalPanel();
		playerPanel.add(player1Box);
		playerPanel.add(vsLabel);
		playerPanel.add(player2Box);
		
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
		HorizontalPanel setPanel = MainApplication.createHorizontalPanel();
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
				// TODO add save code here.
				RegisterResultPanel.this.hide();
			}
		});
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				RegisterResultPanel.this.hide();
			}
		});
		HorizontalPanel buttonPanel = MainApplication.createHorizontalPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		mainPanel.add(new Label("Registrera ny match"));
		mainPanel.add(playerPanel);
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
		HorizontalPanel scorePanel = MainApplication.createHorizontalPanel();
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
		for (Player player : players) {
			playerBox.addItem(player.getPlayerName());
		}
	}
	
}
