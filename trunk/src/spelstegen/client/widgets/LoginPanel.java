package spelstegen.client.widgets;

import spelstegen.client.MainApplication;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.entities.Player;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
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
public class LoginPanel extends DialogBox {
	
	private TextBox usernameBox;
	private PasswordTextBox passwordBox;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;
	
	public LoginPanel(final SpelstegenServiceAsync spelstegenService, final MainApplication mainApplication) {
		super(false);
		setText("Logga in");
		setAnimationEnabled(true);
		
		this.setSize(String.valueOf(WIDTH) + "px", String.valueOf(HEIGHT) + "px");
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		
		HorizontalPanel usernamePanel = MainApplication.createStandardHorizontalPanel();
		Label lblUserName = new Label("Användarnamn (epost):");
		lblUserName.setWidth("200px");
		lblUserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		usernamePanel.add(lblUserName);
		usernameBox = new TextBox();
		usernamePanel.add(usernameBox);
		usernameBox.setWidth("200px");
		
		HorizontalPanel passwordPanel = MainApplication.createStandardHorizontalPanel();
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
				if (result != null) {
					mainApplication.loggedIn(result);
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
		HorizontalPanel buttonPanel = MainApplication.createStandardHorizontalPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
	}

}
