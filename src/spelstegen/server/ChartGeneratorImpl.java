package spelstegen.server;

import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;

import spelstegen.client.ChartGenerator;
import spelstegen.server.chart.Chart;
import spelstegen.server.chart.PointsHistoryChart;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service that generates different kinds of charts, converts them
 * into images and returns image url to the generated chart.
 * 
 * @author Per Mattsson
 */
public class ChartGeneratorImpl extends RemoteServiceServlet implements ChartGenerator {
	
	private static final long serialVersionUID = -1396658608550159806L;
	
    public String generatePointsHistoryChart() {
        
    	Chart chart = new PointsHistoryChart();
    	
    	return saveChartAsPNG(chart);
    }
    
    /**
     * Saves a chart in PNG format
     * 
     * @param chart the chart to save
     * @return filename to chart image
     */
    private String saveChartAsPNG(Chart chart) {
        String chartName = "";
        
        JFreeChart jFreeChart = chart.getChart();
        try {
            HttpSession session = getThreadLocalRequest().getSession();
            chartName = ServletUtilities.saveChartAsPNG(jFreeChart, chart.getWidth(), chart.getHeight(), null, session);
        } catch(Exception e) {
            // handle exception
        }
        return chartName;
    }
} 