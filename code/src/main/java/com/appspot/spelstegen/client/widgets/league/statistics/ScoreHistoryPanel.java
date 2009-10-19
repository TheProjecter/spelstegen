package com.appspot.spelstegen.client.widgets.league.statistics;

import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.MatchDrawException;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Score;
import com.appspot.spelstegen.client.services.MatchListUpdateListener;
import com.appspot.spelstegen.client.services.ScoreCalculator;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.appspot.spelstegen.client.widgets.league.statistics.chart.LineChart;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gchart.client.GChart;

/**
 * Score history panel
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryPanel extends VerticalPanel implements MatchListUpdateListener {

	private final static int VERTICAL_SPACING = 15;
	
	private GChart chart = null;
	
	public ScoreHistoryPanel(int width, int height) {
		ServiceManager.getInstance().addMatchListUpdateListener(this);
		setWidth("100%");
		setHeight("100%");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
	}

	@Override
	public void matchListUpdated(List<Match> matches) {
		Map<Player, List<Score>> playerScoreHistory;
		try {
			playerScoreHistory = ScoreCalculator.calculateScore(matches);
			if (chart != null) {
				remove(chart);
			}
			chart = new LineChart(playerScoreHistory, 450, 400);
			add(chart);
			chart.update();
		} catch (MatchDrawException e) {
			Log.error("MatchDrawException: " + e.getMessage(), e);
		}
	}
}
