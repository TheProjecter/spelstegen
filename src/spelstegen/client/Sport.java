package spelstegen.client;

import java.io.Serializable;

/**
 * Class representing a Sport
 * @author Henrik Segesten
 */
public class Sport implements Serializable {
	private int id;
	private String name;
	private String iconUrl;
	
	
	public Sport(int id, String name, String iconUrl) {
		super();
		this.id = id;
		this.name = name;
		this.iconUrl = iconUrl;
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
	
	
	
	
	
}
