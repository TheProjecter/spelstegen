package spelstegen.client.widgets;

import spelstegen.client.MainApplication;
import spelstegen.client.Player;
import spelstegen.client.SpelstegenServiceAsync;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class that draws the login panel
 * 
 * @author Henrik Segesten
 *
 */
public class LoginPanel extends PopupPanel {
	
	private TextBox usernameBox;
	private PasswordTextBox passwordBox;
	
	public LoginPanel(final SpelstegenServiceAsync spelstegenService, final MainApplication mainApplication) {
		super(false);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		
		mainPanel.add(new Label("Logga in"));
		
		HorizontalPanel usernamePanel = MainApplication.createHorizontalPanel();
		usernamePanel.add(new Label("Användarnamn (epost):"));
		usernameBox = new TextBox();
		usernamePanel.add(usernameBox);
		
		HorizontalPanel passwordPanel = MainApplication.createHorizontalPanel();
		passwordPanel.add(new Label("Lösenord:"));
		passwordBox = new PasswordTextBox();
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
				if (result != null) {
					mainApplication.loggedIn(result);
					MainApplication.showStatus("Inloggad: " + result.getPlayerName());
				} else {
					Window.alert("Fel lösenord eller epostadress, försök igen.");
				}
				LoginPanel.this.hide();
			}
		};
		
		PushButton loginButton = new PushButton("Ok");
		loginButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				spelstegenService.logIn(usernameBox.getText().trim(), Player.md5(passwordBox.getText()), callback);
			}
		});
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				LoginPanel.this.hide();
			}
		});
		HorizontalPanel buttonPanel = MainApplication.createHorizontalPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
	}
}
