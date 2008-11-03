package spelstegen.server.chart;

import java.text.SimpleDateFormat;

import org.jfree.chart.JFreeChart;

/**
 * Common properties for charts
 * 
 * @author Per Mattsson
 */
public abstract class Chart {

	protected static final int DEFAULT_WIDTH = 600;
	protected static final int DEFAULT_HEIGHT = 400;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected int width;
	protected int height;

	public Chart() {
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public abstract JFreeChart getChart();
}
