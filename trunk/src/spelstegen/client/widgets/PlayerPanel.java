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
 * Panel that shows details of a Player
 * @author Henrik Segesten
 */
public class PlayerPanel extends PopupPanel {

	private TextBox nameBox;
	private TextBox emailBox;
	private PasswordTextBox passwordBox;
	private PasswordTextBox passwordBox2;
	
	public PlayerPanel(final SpelstegenServiceAsync spelstegenService, final MainApplication parent) {
		super(false);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(MainApplication.VERTICAL_SPACING);
		
		mainPanel.add(new Label("Lägg till ny spelare"));
		
		HorizontalPanel namePanel = MainApplication.createHorizontalPanel();
		namePanel.add(new Label("Spelarens namn:"));
		nameBox = new TextBox();
		namePanel.add(nameBox);
		
		HorizontalPanel emailPanel = MainApplication.createHorizontalPanel();
		emailPanel.add(new Label("E-postadress:"));
		emailBox = new TextBox();
		emailPanel.add(emailBox);
		
		HorizontalPanel passwordPanel = MainApplication.createHorizontalPanel();
		passwordPanel.add(new Label("Lösenord:"));
		passwordBox = new PasswordTextBox();
		passwordPanel.add(passwordBox);
		
		HorizontalPanel repeatPwPanel = MainApplication.createHorizontalPanel();
		repeatPwPanel.add(new Label("Upprepa lösenordet:"));
		passwordBox2 = new PasswordTextBox();
		repeatPwPanel.add(passwordBox2);
		
		VerticalPanel fieldPanel = new VerticalPanel();
		fieldPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fieldPanel.setSpacing(10);
		fieldPanel.add(namePanel);
		fieldPanel.add(emailPanel);
		fieldPanel.add(passwordPanel);
		fieldPanel.add(repeatPwPanel);
		
		final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				Window.alert("Fel vid sparning av spelare. " + caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				if (result) {
					MainApplication.showStatus("Sparade spelaren.");
					parent.updatePlayerList();
					parent.populateTable();
					PlayerPanel.this.hide();
				} else {
					Window.alert("Emailadressen är inte unik, försök igen");
				}
			}
			
		};
		
		PushButton createButton = new PushButton("Spara");
		createButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (passwordBox.getText().trim().equals(passwordBox2.getText().trim())) {
					Player player = new Player(nameBox.getText().trim(), emailBox.getText().trim());
					player.changePassword(passwordBox.getText().trim());
					spelstegenService.addPlayer(player, callback);
				}
			}
		});
		
		PushButton cancelButton = new PushButton("Avbryt");
		cancelButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				PlayerPanel.this.hide();
			}
		});
		
		HorizontalPanel buttonPanel = MainApplication.createHorizontalPanel();
		buttonPanel.add(createButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
	}
	
}
