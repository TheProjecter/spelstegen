package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Class representing a Sport.
 * 
 * @author Henrik Segesten
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Integer id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String iconUrl;
	
	@Persistent
	private List<Sport> childSports;

	public Sport() {
		this.childSports = new ArrayList<Sport>();
	}
	
	public Sport(Integer sportId, String name, String iconUrl, List<Sport> childSports) {
		this.id = sportId;
		this.name = name;
		this.iconUrl = iconUrl;
		this.childSports = childSports;
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<Sport> getChildSports() {
		return childSports;
	}

	public void setChildSports(List<Sport> childSports) {
		this.childSports = childSports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sport other = (Sport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
