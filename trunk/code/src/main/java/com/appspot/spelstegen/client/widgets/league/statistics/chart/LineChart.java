package com.appspot.spelstegen.client.widgets.league.statistics.chart;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Score;
import com.appspot.spelstegen.client.services.ScoreCalculator;
import com.googlecode.gchart.client.GChart;

public class LineChart extends GChart {

	public LineChart(Map<Player, List<Score>> playerScoreHistory, int width, int height) {
		super(width, height);
		setPadding("10px");

		GChart.setDefaultSymbolBorderColors(new String[] { "#004586",
				"#ff420e", "#ffd320", "#579d1c", "#7e0021", "#83caff",
				"#314004", "#aecf00", "#4b1f6f", "#ff950e", "#c5000b",
				"#0084d1" });
		getXAxis().setAxisLabel("<small><b><i>Time</i></b></small>");
		getXAxis().setHasGridlines(true);
		getXAxis().setTickCount(6);
		getXAxis().setTickLabelFormat("=(Date)yyyy-MM-dd");

		getYAxis().setAxisLabel("<small><b><i>Score</i></b></small>");
		getYAxis().setHasGridlines(true);
		getYAxis().setTickCount(11);
		int minScore = ScoreCalculator.getLowestScore(playerScoreHistory);
		int maxScore = ScoreCalculator.getHighestScore(playerScoreHistory);
		getYAxis().setAxisMin(minScore);
		getYAxis().setAxisMax(maxScore);

		for (Entry<Player, List<Score>> entry : playerScoreHistory.entrySet()) {
			addCurve(entry.getKey(), entry.getValue());
		}
	}

	private void addCurve(Player player,  List<Score> scoreHistory) {
		addCurve();
		getCurve().setLegendLabel(
				"<i>" + player.getPlayerName() + "</i>");
		getCurve().getSymbol().setSymbolType(SymbolType.LINE);

		for (Score score : scoreHistory) {
			getCurve().addPoint(score.getDate().getTime(), score.getScore());
		}
	}
}