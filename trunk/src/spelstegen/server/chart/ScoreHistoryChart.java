package spelstegen.server.chart;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import spelstegen.client.entities.Player;
import spelstegen.client.entities.Score;

/**
 * Chart that displays history of points for players
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryChart extends Chart {

	private JFreeChart chart;
	
	/**
	 * Constructor
	 */
	public ScoreHistoryChart(Map<Player, List<Score>> playerScoreHistory) {
		chart = ChartFactory.createTimeSeriesChart(
				"", // title
				"", // x-axis label
				"", // y-axis label
				getDataset(playerScoreHistory), // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setVerticalTickLabels(true);
		axis.setDateFormatOverride(sdf);
	}
	
	@Override
	public JFreeChart getChart() {
		return chart;
	}
	
	/**
	 * Get dataset to display in chart.
	 * 
	 * @return the dataset
	 */
	private XYDataset getDataset(Map<Player, List<Score>> playerScoreHistory) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		for (Iterator<Map.Entry<Player, List<Score>>> it = playerScoreHistory.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Player, List<Score>> pScoreHistory = (Map.Entry<Player, List<Score>>) it.next();
			Player player = pScoreHistory.getKey();
			List<Score> scoreHistory = pScoreHistory.getValue();
			TimeSeries ts = new TimeSeries(player.getPlayerName(), Day.class);
			for (Score score : scoreHistory) {
				ts.addOrUpdate(new Day(score.getDate()), score.getScore());
			}
			dataset.addSeries(ts);
		}
		return dataset;
	}
}
