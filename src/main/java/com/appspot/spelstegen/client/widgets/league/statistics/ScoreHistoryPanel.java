package com.appspot.spelstegen.client.widgets.league.statistics;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Score history panel
 * 
 * @author Per Mattsson
 */
public class ScoreHistoryPanel extends VerticalPanel {

	private final static int VERTICAL_SPACING = 15;
	
	public ScoreHistoryPanel(int width, int height) {
		setWidth("100%");
		setHeight("100%");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
}
