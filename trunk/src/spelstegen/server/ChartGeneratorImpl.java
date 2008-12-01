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
	
    public String generateScoreHistoryChart(League league, Season season) {
        
    	List<Match> matches = storage.getMatches(league);
    	try {
			Map<Player, List<Score>> playerScoreHistory = LadderCalculator.calculateScore(matches);
			Chart chart = new ScoreHistoryChart(playerScoreHistory);
	    	return saveChartAsPNG(chart);
		} catch (MatchDrawException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
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