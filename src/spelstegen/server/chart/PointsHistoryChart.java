package spelstegen.server.chart;

import java.awt.Color;
import java.text.ParseException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Chart that displays history of points for players
 * 
 * @author Per Mattsson
 */
public class PointsHistoryChart extends Chart {

	private JFreeChart chart;
	
	/**
	 * Constructor
	 */
	public PointsHistoryChart() {
		chart = ChartFactory.createTimeSeriesChart(
				"Poänghistorik", // title
				"", // x-axis label
				"", // y-axis label
				getDataset(), // data
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
	 * TODO: Replace test data with data from DB
	 */
	private XYDataset getDataset() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		try {
			TimeSeries s1 = new TimeSeries("Ernst-Hugo", Day.class);
			s1.add(new Day(sdf.parse("2008-10-02")), 1000);
			s1.add(new Day(sdf.parse("2008-10-06")), 900);
			s1.add(new Day(sdf.parse("2008-10-10")), 934);
			s1.add(new Day(sdf.parse("2008-10-20")), 720);
			s1.add(new Day(sdf.parse("2008-10-28")), 1200);
			
			TimeSeries s2 = new TimeSeries("Reidar", Day.class);
			s2.add(new Day(sdf.parse("2008-10-02")), 1000);
			s2.add(new Day(sdf.parse("2008-10-06")), 1100);
			s2.add(new Day(sdf.parse("2008-10-10")), 863);
			s2.add(new Day(sdf.parse("2008-10-20")), 720);
			s2.add(new Day(sdf.parse("2008-10-28")), 630);
			
			dataset.addSeries(s1);
			dataset.addSeries(s2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataset;
	}
}
