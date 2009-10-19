package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.appspot.spelstegen.client.services.ScoreCalculator;

/**
 * Match class
 * 
 * @author Henrik Segesten
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Match implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Integer id;
	
	@Persistent
	private Integer leagueId;
	
	@Persistent
	private Date date;
	
	@Persistent
	private Sport sport;
	
	@Persistent
	private Player player1;
	
	@Persistent
	private Player player2;
	
	@Persistent
	private List<Set> sets;
	
	public Match() {
		sets = new ArrayList<Set>();
	}

	public Match(Integer leagueId, Date date, Sport sport, Player player1,
			Player player2) {
		this();
		this.leagueId = leagueId;
		this.date = date;
		this.sport = sport;
		this.player1 = player1;
		this.player2 = player2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Integer leagueId) {
		this.leagueId = leagueId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public List<Set> getSets() {
		return sets;
	}

	public void setSets(List<Set> sets) {
		this.sets = sets;
	}
	
	public void addSet(Set set) {
		sets.add(set);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String toString() {
		return "Match " + id + ", date: " + date.toString() + ", sport: " + sport.getName() + ", player1: " + player1
		+ ", player2: " + player2 + ", sets: " + ScoreCalculator.getResultsString(this); 
	}
}
