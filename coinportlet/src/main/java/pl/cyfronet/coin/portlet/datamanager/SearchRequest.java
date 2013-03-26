package pl.cyfronet.coin.portlet.datamanager;

public class SearchRequest {
	private String name;
	private String startModificationDate;
	private String stopModificationDate;
	private String startCreationDate;
	private String stopCreationDate;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getStartModificationDate() {
		return startModificationDate;
	}
	public void setStartModificationDate(String startModificationDate) {
		this.startModificationDate = startModificationDate;
	}

	public String getStopModificationDate() {
		return stopModificationDate;
	}
	public void setStopModificationDate(String stopModificationDate) {
		this.stopModificationDate = stopModificationDate;
	}
	
	public String getStartCreationDate() {
		return startCreationDate;
	}
	public void setStartCreationDate(String startCreationDate) {
		this.startCreationDate = startCreationDate;
	}
	
	public String getStopCreationDate() {
		return stopCreationDate;
	}
	public void setStopCreationDate(String stopCreationDate) {
		this.stopCreationDate = stopCreationDate;
	}	
}