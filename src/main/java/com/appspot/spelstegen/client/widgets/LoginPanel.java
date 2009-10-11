package com.appspot.spelstegen.client.widgets;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
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
 * Class that draws the login panel
 * 
 * @author Henrik Segesten
 *
 */
public class LoginPanel extends DialogBox {
	
	private static final int KEY_ENTER = 13;
	private TextBox usernameBox;
	private PasswordTextBox passwordBox;
	
	public LoginPanel() {
		super(false);
		setText("Logga in");
		setAnimationEnabled(true);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(Spelstegen.VERTICAL_SPACING);
		
		HorizontalPanel usernamePanel = Spelstegen.createStandardHorizontalPanel();
		Label lblUserName = new Label("Användarnamn (epost):");
		lblUserName.setWidth("200px");
		lblUserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		usernamePanel.add(lblUserName);
		usernameBox = new TextBox();
		usernamePanel.add(usernameBox);
		usernameBox.setWidth("200px");
		
		HorizontalPanel passwordPanel = Spelstegen.createStandardHorizontalPanel();
		Label lblPassword = new Label("Lösenord:");
		lblPassword.setWidth("200px");
		lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		passwordPanel.add(lblPassword);
		passwordBox = new PasswordTextBox();
		passwordBox.setWidth("200px");
		passwordPanel.add(passwordBox);
		
		VerticalPanel fieldPanel = new VerticalPanel();
		fieldPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fieldPanel.setSpacing(10);
		fieldPanel.add(usernamePanel);
		fieldPanel.add(passwordPanel);
		
		final AsyncCallback<Player> callback = new AsyncCallback<Player>() {
			public void onFailure(Throwable caught) {
				Window.alert("Inloggningen misslyckades. " + caught.getMessage());
			}
			public void onSuccess(Player result) {
				if (result == null) {
					Window.alert("Fel lösenord eller epostadress, försök igen.");
				}
				LoginPanel.this.hide();
			}
		};
		
		final Command loginCommand = new Command() {
			public void execute() {
				ServiceManager.getInstance().logIn(usernameBox.getText().trim(), Player.md5(passwordBox.getText()), callback);
			}
		};
		
		KeyPressHandler keyboardListener = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KEY_ENTER) {
					loginCommand.execute();
				}
			}
		};
		usernameBox.addKeyPressHandler(keyboardListener);
		passwordBox.addKeyPressHandler(keyboardListener);
		
		PushButton loginButton = new PushButton("Ok");
		loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginCommand.execute();
			}
		});
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoginPanel.this.hide();
			}
		});
		HorizontalPanel buttonPanel = Spelstegen.createStandardHorizontalPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
	}

	public void setUserNameFocus(boolean focused) {
		usernameBox.setFocus(focused);
	}
}
