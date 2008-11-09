package spelstegen.client;

import java.io.Serializable;
import java.util.Date;

/**
 * Class representing a season
 * @author Henrik Segesten
 */
public class Season implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
	
	public Season(String name, Date startDate, Date endDate) {
		this(0, name, startDate, endDate);
	}
	
	public Season(int id, String name, Date startDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
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
