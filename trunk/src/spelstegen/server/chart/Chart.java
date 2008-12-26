package spelstegen.server.chart;

import java.text.SimpleDateFormat;

import org.jfree.chart.JFreeChart;

/**
 * Common properties for charts
 * 
 * @author Per Mattsson
 */
public abstract class Chart {

	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public abstract JFreeChart getChart();
}