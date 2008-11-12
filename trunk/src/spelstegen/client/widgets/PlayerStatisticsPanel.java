/**
 * 
 */
package spelstegen.client.widgets;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Player statistics
 * 
 * @author Per Mattsson
 */
public class PlayerStatisticsPanel extends VerticalPanel {

	private final static int VERTICAL_SPACING = 15;
	
	public PlayerStatisticsPanel() {
		setWidth("500px");
		setHeight("500px");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
}
