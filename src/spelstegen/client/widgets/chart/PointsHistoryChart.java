package spelstegen.client.widgets.chart;

/**
 * PointsHistoryChart
 * 
 * @author Per Mattsson
 */
public class PointsHistoryChart extends AbstractChart {

    public PointsHistoryChart() {
        super();
        // Make the call to the chart generator service with the previously created callback.
        chartGeneratorService.generatePointsHistoryChart(callback);
    }
}
