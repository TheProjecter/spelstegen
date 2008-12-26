package spelstegen.server;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;

import spelstegen.client.ChartGenerator;
import spelstegen.client.LadderCalculator;
import spelstegen.client.entities.League;
import spelstegen.client.entities.Match;
import spelstegen.client.entities.MatchDrawException;
import spelstegen.client.entities.Player;
import spelstegen.client.entities.Score;
import spelstegen.client.entities.Season;
import spelstegen.server.chart.Chart;
import spelstegen.server.chart.ScoreHistoryChart;
import spelstegen.server.chart.TestPieChart;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service that generates different kinds of charts, converts them
 * into images and returns image url to the generated chart.
 * 
 * @author Per Mattsson
 */
public class ChartGeneratorImpl extends RemoteServiceServlet implements ChartGenerator {
	
	private static final long serialVersionUID = -1396658608550159806L; 
	
	protected StorageInterface storage = new MySQLStorageImpl();
	
    public String generateScoreHistoryChart(League league, Season season, int width, int height) {
        
    	List<Match> matches = storage.getMatches(league);
    	try {
			Map<Player, List<Score>> playerScoreHistory = LadderCalculator.calculateScore(matches);
			Chart chart = new ScoreHistoryChart(playerScoreHistory);
	    	return saveChartAsPNG(chart, width, height);
		} catch (MatchDrawException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }
    
    public String generatePieChart(int width, int height) {
        
    	Chart chart = new TestPieChart();
    	return saveChartAsPNG(chart, width, height);
    }
    
    /**
     * Saves a chart in PNG format
     * 
     * @param chart the chart to save
     * @param width image width
     * @param height image height
     * @return filename to chart image
     */
    private String saveChartAsPNG(Chart chart, int width, int height) {
        String chartName = "";
        
        JFreeChart jFreeChart = chart.getChart();
        try {
            HttpSession session = getThreadLocalRequest().getSession();
            chartName = ServletUtilities.saveChartAsPNG(jFreeChart, width, height, null, session);
        } catch(Exception e) {
            // handle exception
        }
        return chartName;
    }
} 