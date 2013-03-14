package pl.cyfronet.coin.portlet.datamanager;

public class SearchRequest {
	private String name;
	private String startModificationDate;
	private String stopModificationDate;

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
}