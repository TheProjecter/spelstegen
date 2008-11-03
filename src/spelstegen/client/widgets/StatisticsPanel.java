package spelstegen.client.widgets;

import spelstegen.client.widgets.chart.PointsHistoryChart;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Statistics panel
 * 
 * @author Per Mattsson
 */
public class StatisticsPanel extends VerticalPanel {
	
	private final static int VERTICAL_SPACING = 15;

	public StatisticsPanel() {
		setWidth("100%");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		add(new PointsHistoryChart());
	}
}
