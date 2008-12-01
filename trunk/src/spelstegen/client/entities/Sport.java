package spelstegen.client.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing a Sport.
 * 
 * @author Henrik Segesten
 */
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String iconUrl;
	private List<Sport> childSports;

	public Sport() {}
	
	public Sport(Integer sportId, String name, String iconUrl, List<Sport> childSports) {
		this.id = sportId;
		this.name = name;
		this.iconUrl = iconUrl;
		this.childSports = childSports;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
}
