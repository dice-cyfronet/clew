package pl.cyfronet.coin.portlet.lobcder;

public class LobcderSearchCriteria {
	private long modificationStartMillis;
	private long modificationStopMillis;
	private long creationStartMillis;
	private long creationStopMillis;
	private String name;
	
	public long getModificationStartMillis() {
		return modificationStartMillis;
	}
	public void setModificationStartMillis(long modificationStartMillis) {
		this.modificationStartMillis = modificationStartMillis;
	}
	
	public long getModificationStopMillis() {
		return modificationStopMillis;
	}
	public void setModificationStopMillis(long modificationStopMillis) {
		this.modificationStopMillis = modificationStopMillis;
	}
	
	public long getCreationStartMillis() {
		return creationStartMillis;
	}
	public void setCreationStartMillis(long creationStartMillis) {
		this.creationStartMillis = creationStartMillis;
	}
	
	public long getCreationStopMillis() {
		return creationStopMillis;
	}
	public void setCreationStopMillis(long creationStopMillis) {
		this.creationStopMillis = creationStopMillis;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}