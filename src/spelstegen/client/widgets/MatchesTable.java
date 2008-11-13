/**
 * 
 */
package spelstegen.client.widgets;

import java.util.List;

import spelstegen.client.LadderCalculator;
import spelstegen.client.League;
import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.LeagueUpdater;
import spelstegen.client.Match;
import spelstegen.client.MatchDrawException;
import spelstegen.client.Player;
import spelstegen.client.SpelstegenServiceAsync;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Displays all match results in a league
 * 
 * @author Per Mattsson
 */
public class MatchesTable extends VerticalPanel implements LeagueUpdateListener {

	private GetMatchesCallback getMatchesCallback = new GetMatchesCallback();
	private Grid matchTable;
	private SpelstegenServiceAsync spelstegenService;
	private List<Match> matches;
	
	public MatchesTable(SpelstegenServiceAsync spelstegenService, LeagueUpdater leagueUpdater) {
		this.spelstegenService = spelstegenService;
		leagueUpdater.addLeagueUpdateListener(this);
		matchTable = new Grid(3, 4);
		matchTable.setCellSpacing(0);
		matchTable.setWidth("600px");
		add(matchTable);
	}
	
	private void populateMatches() {
		matchTable.resize(matches.size(), 4);
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(i);
			matchTable.setText(i, 0, match.getDate().toString());
			matchTable.setHTML(i, 1, getPlayerNameHtml(match, match.getPlayer1()));
			matchTable.setHTML(i, 2, getPlayerNameHtml(match, match.getPlayer2()));
			matchTable.setText(i, 3, LadderCalculator.getResultsString(match));
		}
	}
	
	/**
	 * Returns player name. If player is winner of match, name will be
	 * returned in bold; otherwise plain text.
	 */
	private String getPlayerNameHtml(Match match, Player player) {
		try {
			boolean isWinner = LadderCalculator.getWinner(match).equals(player);
			if (isWinner) {
				return "<b>"+player.getPlayerName()+"</b>";
			}
		} catch (MatchDrawException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return player.getPlayerName();
	}
	
	public void leagueUpdated(League league) {
		spelstegenService.getMatches(league, getMatchesCallback);
	}
	
	private class GetMatchesCallback implements AsyncCallback<List<Match>> {

		public void onFailure(Throwable caught) {
			Window.alert("Failed to get matches. " + caught.getMessage());
		}

		public void onSuccess(List<Match> result) {
			matches = result;
			populateMatches();
		}    	
    }
}
