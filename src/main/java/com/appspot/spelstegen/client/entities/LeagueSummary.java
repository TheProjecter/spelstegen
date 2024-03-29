package com.appspot.spelstegen.client.entities;

import java.io.Serializable;

/**
 * This class is used to send quick summaries of a league.
 * @author Henrik Segesten
 */
public class LeagueSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private int numberOfPlayers;
	
	public LeagueSummary() {
		super();
	}

	public LeagueSummary(Long id, String name, int numberOfPlayers) {
		this.id = id;
		this.name = name;
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
}
