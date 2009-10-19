package com.appspot.spelstegen.client.widgets.league.statistics;

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
		setWidth("100%");
		setHeight(String.valueOf(StatisticsPanel.HEIGHT) + "px");
		setSpacing(VERTICAL_SPACING);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
}
