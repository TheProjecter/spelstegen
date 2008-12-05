package spelstegen.client.widgets;

import java.util.List;

import spelstegen.client.LeagueChanger;
import spelstegen.client.MainApplication;
import spelstegen.client.SpelstegenServiceAsync;
import spelstegen.client.entities.LeagueSummary;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is the start page of the spelstegen client.
 * 
 * @author Henrik Segesten
 */
public class OverviewPanel extends Composite implements HistoryListener {
	
	private VerticalPanel leaguesPanel;
	private LeagueChanger leagueChanger;
	
	public OverviewPanel(SpelstegenServiceAsync spelstegenServiceAsync, LeagueChanger leagueChanger) {
		this.leagueChanger = leagueChanger;
		
		leaguesPanel = new VerticalPanel();
		leaguesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ScrollPanel scrollPanel = new ScrollPanel(leaguesPanel);
		scrollPanel.setSize("780px", "500px");
		//mainPanel.add(leaguesPanel);
		History.addHistoryListener(this);
		initWidget(scrollPanel);
		AsyncCallback<List<LeagueSummary>> callback = new AsyncCallback<List<LeagueSummary>>() {

			public void onFailure(Throwable arg0) {
				Window.alert("Misslyckades att hämta ligor. " + arg0.getMessage());
			}

			public void onSuccess(List<LeagueSummary> result) {
				for (LeagueSummary league : result) {
					addLeagueRow(league);
				}
			}
			
		};
		spelstegenServiceAsync.getLeagueSummaries(callback);
	}
	
	private void addLeagueRow(LeagueSummary leagueSummary) {
		Hyperlink leagueLink = new Hyperlink(leagueSummary.getName(), leagueSummary.getId()+"");
		leagueLink.setStylePrimaryName("toplabel");
		Label nrMembers = new Label(leagueSummary.getNumberOfPlayers() + " spelare");
		HorizontalPanel firstRow = MainApplication.createStandardHorizontalPanel();
		firstRow.add(leagueLink);
		firstRow.add(nrMembers);
		leaguesPanel.add(firstRow);
	}

	public void onHistoryChanged(String arg) {
		int leagueId = Integer.parseInt(arg.trim());
		leagueChanger.changeToLeague(leagueId);
	}
	
}
