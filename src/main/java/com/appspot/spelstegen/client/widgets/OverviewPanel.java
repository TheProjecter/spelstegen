package com.appspot.spelstegen.client.widgets;

import java.util.List;

import com.appspot.spelstegen.client.Spelstegen;
import com.appspot.spelstegen.client.entities.LeagueSummary;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
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
public class OverviewPanel extends Composite implements ValueChangeHandler<String>  {
	
	private VerticalPanel leaguesPanel;
	
	public OverviewPanel() {
		leaguesPanel = new VerticalPanel();
		leaguesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ScrollPanel scrollPanel = new ScrollPanel(leaguesPanel);
		scrollPanel.setSize("780px", "500px");
		History.addValueChangeHandler(this);
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
		ServiceManager.getInstance().getLeagueSummaries(callback);
	}
	
	private void addLeagueRow(LeagueSummary leagueSummary) {
		Hyperlink leagueLink = new Hyperlink(leagueSummary.getName(), leagueSummary.getId()+"");
		leagueLink.setStylePrimaryName("toplabel");
		Label nrMembers = new Label(leagueSummary.getNumberOfPlayers() + " spelare");
		HorizontalPanel firstRow = Spelstegen.createStandardHorizontalPanel();
		firstRow.add(leagueLink);
		firstRow.add(nrMembers);
		leaguesPanel.add(firstRow);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event != null && !event.getValue().equals("")) {
			Long leagueId = Long.valueOf(event.getValue().trim());
			ServiceManager.getInstance().getLeague(leagueId);
		}
	}
	
}
