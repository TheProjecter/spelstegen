package com.appspot.spelstegen.client.widgets;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel that shows details of a Player
 * @author Henrik Segesten
 */
public class PlayerPanel extends DialogBox {

	private TextBox nameBox;
	private TextBox emailBox;
	private TextBox nickNameBox;
	private TextBox imageUrlBox;
	private PasswordTextBox passwordBox;
	private PasswordTextBox passwordBox2;
	
	public PlayerPanel(final Player player, final League currentLeague) {
		super(false);
		setAnimationEnabled(true);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(Spelstegen.VERTICAL_SPACING);
		
		if (player == null) {
			setText("Lägg till ny spelare");
		} else {
			setText("Spelarprofil");
		}
		
		HorizontalPanel namePanel = Spelstegen.createStandardHorizontalPanel();
		namePanel.add(new Label("Namn:"));
		nameBox = new TextBox();
		if (player != null) {
			nameBox.setText(player.getPlayerName());
		}
		namePanel.add(nameBox);
		
		HorizontalPanel emailPanel = Spelstegen.createStandardHorizontalPanel();
		emailPanel.add(new Label("E-postadress:"));
		emailBox = new TextBox();
		if (player != null) {
			emailBox.setText(player.getEmail());
		}
		emailPanel.add(emailBox);
		
		HorizontalPanel nickNamePanel = Spelstegen.createStandardHorizontalPanel();
		nickNamePanel.add(new Label("Smeknamn:"));
		nickNameBox = new TextBox();
		if (player != null) {
			nickNameBox.setText(player.getNickName());
		}
		nickNamePanel.add(nickNameBox);
		
		HorizontalPanel imageUrlPanel = Spelstegen.createStandardHorizontalPanel();
		imageUrlPanel.add(new Label("Avatar URL:"));
		imageUrlBox = new TextBox();
		if (player != null) {
			imageUrlBox.setText(player.getImageURL());
		}
		imageUrlPanel.add(imageUrlBox);
		
		HorizontalPanel passwordPanel = Spelstegen.createStandardHorizontalPanel();
		passwordPanel.add(new Label("Lösenord:"));
		passwordBox = new PasswordTextBox();
		passwordPanel.add(passwordBox);
		
		HorizontalPanel repeatPwPanel = Spelstegen.createStandardHorizontalPanel();
		repeatPwPanel.add(new Label("Upprepa lösenordet:"));
		passwordBox2 = new PasswordTextBox();
		repeatPwPanel.add(passwordBox2);
		
		VerticalPanel fieldPanel = new VerticalPanel();
		fieldPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fieldPanel.setSpacing(10);
		fieldPanel.add(namePanel);
		fieldPanel.add(emailPanel);
		fieldPanel.add(nickNamePanel);
		fieldPanel.add(imageUrlPanel);
		fieldPanel.add(passwordPanel);
		fieldPanel.add(repeatPwPanel);
		
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Fel vid sparning av spelare. " + caught.getMessage());
			}
			public void onSuccess(Void arg) {
				Spelstegen.showStatus("Sparade spelaren.");
				PlayerPanel.this.hide();
			}
		};
		
		PushButton saveButton = new PushButton("Spara");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (player != null) {
					player.setEmail(emailBox.getText().trim());
					player.setPlayerName(nameBox.getText().trim());
					player.setNickName(nickNameBox.getText().trim());
					player.setImageURL(imageUrlBox.getText().trim());
					String pw = getNewPassword();
					if (pw != null && !pw.equals("")) {
						player.changePassword(pw);
					}
				} else {
					Player player = new Player(nameBox.getText().trim(),
							emailBox.getText().trim());
					String pw = getNewPassword();
					player.changePassword(pw);

				}
				ServiceManager.getInstance().savePlayer(player, currentLeague, callback);
			}
		});
		
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PlayerPanel.this.hide();				
			}
		});
		
		HorizontalPanel buttonPanel = Spelstegen.createStandardHorizontalPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
	}
	
	private String getNewPassword() {
		if (passwordBox.getText().trim().equals("") && passwordBox2.getText().trim().equals("")) {
			return "";
		}
		if (passwordBox.getText().trim().equals(passwordBox2.getText().trim())) {
			return passwordBox.getText().trim();
		} else {
			Window.alert("Lösenorden matchar inte. Försök igen.");
			return null;
		}
	}
	
}
