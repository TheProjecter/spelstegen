package spelstegen.server.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class TestPieChart extends Chart {

	@Override
	public JFreeChart getChart() {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("JavaWorld", new Integer(75));
		pieDataset.setValue("Other", new Integer(25));
		JFreeChart chart = ChartFactory.createPieChart
		                     ("Sample Pie Chart",   // Title
		                      pieDataset,           // Dataset
		                      true, false, false                  // Show legend  
		                     );
		return chart;
	}

}
