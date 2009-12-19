package com.appspot.spelstegen.client.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A panel that is visible for a certain amount of time.
 * 
 * @author Per Mattsson
 */
public class SplashPanel extends PopupPanel {
	
	private Timer timeoutTimer = null;

	public SplashPanel() {
		super(true);
		setAnimationEnabled(true);
	}
	
	public void show(String text, int timeoutMs) {
		timeoutTimer = new Timer() {
			public void run() {
				timeoutTimer = null;
				SplashPanel.this.hide();
			}
		};
		setWidget(new HTML(text));
		show();
		timeoutTimer.schedule(timeoutMs);

	}

	public void hide() {
		if (timeoutTimer != null) {
			timeoutTimer.cancel();
			timeoutTimer = null;
		}
		super.hide();
	}
}
