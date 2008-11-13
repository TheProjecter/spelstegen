/**
 * 
 */
package spelstegen.client.widgets;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Head-to-head comparison panel
 * 
 * @author Per Mattsson
 */
public class HeadToHeadComparisonPanel extends VerticalPanel {

	private final static int VERTICAL_SPACING = 15;
	
	public HeadToHeadComparisonPanel() {
		setWidth("100%");
		setHeight("500");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
}
