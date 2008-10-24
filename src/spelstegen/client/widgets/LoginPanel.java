package spelstegen.client.widgets;

import spelstegen.client.MainApplication;

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

public class LoginPanel extends PopupPanel {

	public LoginPanel() {
		super(false);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		
		HorizontalPanel usernamePanel = MainApplication.createHorizontalPanel();
		usernamePanel.add(new Label("Användarnamn (epost):"));
		TextBox usernameBox = new TextBox();
		usernamePanel.add(usernameBox);
		
		HorizontalPanel passwordPanel = MainApplication.createHorizontalPanel();
		passwordPanel.add(new Label("Lösenord:"));
		PasswordTextBox passwordBox = new PasswordTextBox();
		passwordPanel.add(passwordBox);
		
		VerticalPanel fieldPanel = new VerticalPanel();
		fieldPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fieldPanel.setSpacing(10);
		fieldPanel.add(usernamePanel);
		fieldPanel.add(passwordPanel);
		
		PushButton loginButton = new PushButton("Logga in");
		loginButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// TODO add login code here
				LoginPanel.this.hide();
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
