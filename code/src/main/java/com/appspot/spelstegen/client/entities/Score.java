package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents player score for a specific date/time
 * 
 * @author Per Mattsson
 */
public class Score implements Serializable, Comparable<Score> {

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private int score;
	
	public Score() {}
	
	public Score(Date date, int score) {
		this.date = date;
		this.score = score;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int compareTo(Score dateScore) {
		return date.compareTo(dateScore.getDate());
	}
}
