/**
 * 
 */
package spelstegen.client.widgets;

import spelstegen.client.LeagueUpdater;
import spelstegen.client.LeagueUpdateListener;
import spelstegen.client.entities.League;
import spelstegen.client.entities.Player;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Main panel for leagues that displays players sorted by highest score
 * 
 * @author Per Mattsson
 */
public class MainLeaguePanel extends VerticalPanel implements LeagueUpdateListener {

	private final static int NUMBER_OF_COLUMNS = 3;
	
	private League league;
	private Grid mainTable;
	
	public MainLeaguePanel(LeagueUpdater leagueUpdater) {
		leagueUpdater.addLeagueUpdateListener(this);
		mainTable = new Grid(1, NUMBER_OF_COLUMNS);
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		this.setSpacing(10);
		mainTable.setCellSpacing(0);
		mainTable.setWidth("100%");
		mainTable.setText(0, 0, "#");
		mainTable.setText(0, 1, "Spelare");
		mainTable.setText(0, 2, "Poäng");
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(0, i, "table-caption"); 
		}
		add(mainTable);
	}

	public void leagueUpdated(League league) {
		this.league = league;
		updateTable();
	}
	
	/**
	 * Updates table
	 */
	private void updateTable() {
	
		int numberOfPlayers = league.getPlayers().size();
		int col = 0;
		Player p = null;
		mainTable.resize(numberOfPlayers+1, NUMBER_OF_COLUMNS);
		for (int i = 0; i < numberOfPlayers; i++) {
			p = league.getPlayers().get(i);
			mainTable.setText(i+1, col++, i+1 + "");
			mainTable.setText(i+1, col++, p.getPlayerName());
			mainTable.setText(i+1, col++, p.getPoints() + "");
			col = 0;
		}
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			mainTable.getCellFormatter().setStyleName(numberOfPlayers, i, "table-caption");
		}
	}
}
