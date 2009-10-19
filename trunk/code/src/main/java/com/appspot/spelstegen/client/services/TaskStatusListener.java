package com.appspot.spelstegen.client.services;

/**
 * Interface to listeners who want to get notified if a task succeeded or failed
 * 
 * @author Per Mattsson
 */
public interface TaskStatusListener {

	/**
	 * Called if the task succeeded
	 */
	public void success();

	/**
	 * Called if the task failed
	 * 
	 * @param reason
	 *            an explanation of why the task failed
	 */
	public void failed(String reason);
}
