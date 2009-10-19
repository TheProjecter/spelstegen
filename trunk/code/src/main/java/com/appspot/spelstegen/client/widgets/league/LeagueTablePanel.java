package com.appspot.spelstegen.client.widgets.league;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.MatchDrawException;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.LeagueUpdateListener;
import com.appspot.spelstegen.client.services.MatchListUpdateListener;
import com.appspot.spelstegen.client.services.ScoreCalculator;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel for leagues that displays players sorted by highest score
 * 
 * @author Per Mattsson
 */
public class LeagueTablePanel extends VerticalPanel implements MatchListUpdateListener, LeagueUpdateListener {

	private final static int NUMBER_OF_COLUMNS = 3;
	
	private League league;
	private List<Match> matches;

	private Grid mainTable;
	
	public LeagueTablePanel() {
		ServiceManager.getInstance().addLeagueUpdateListener(this);
		ServiceManager.getInstance().addMatchListUpdateListener(this);
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
	
	/**
	 * Updates table
	 */
	private void updateTable() {
	
		try {
			Set<Entry<Player, Integer>> playerScores = ScoreCalculator
					.calculateCurrentScoreForPlayers(league.getPlayers(),
							matches);
			int numberOfPlayers = playerScores.size();
			int col = 0;
			Player p = null;
			mainTable.resize(numberOfPlayers+1, NUMBER_OF_COLUMNS);
			int i = 0;
			for (Entry<Player, Integer> entry : playerScores) {
				p = entry.getKey();
				int points = entry.getValue();
				mainTable.setText(i+1, col++, i+1 + "");
				mainTable.setText(i+1, col++, p.getPlayerName());
				mainTable.setText(i+1, col++, points + "");
				col = 0;
				i++;
			}
			for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
				mainTable.getCellFormatter().setStyleName(numberOfPlayers, j, "table-caption");
			}
		} catch (MatchDrawException e) {
			GWT.log(e.getMessage(), e);
		}
	}
	
	@Override
	public void leagueUpdated(League league) {
		this.league = league;
	}
	
	@Override
	public void matchListUpdated(List<Match> matches) {
		this.matches = matches;
		updateTable();
	}
}
