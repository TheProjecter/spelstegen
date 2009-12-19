package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Class representing a season
 * 
 * @author Henrik Segesten
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Season implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Integer id;
	
	@Persistent
	private String name;
	
	@Persistent
	private Date startDate;
	
	@Persistent
	private Date endDate;
	
	public Season() {}
	
	public Season(String name, Date startDate, Date endDate) {
		this(null, name, startDate, endDate);
	}
	
	public Season(Integer id, String name, Date startDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public static Season createInitalSeason() {
		Season season = new Season("", new Date(), null);
		return season;
	}
}
