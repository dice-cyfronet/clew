package pl.cyfronet.coin.portlet.lobcder;

public class LobcderSearchCriteria {
	private long modificationStartMillis;
	private long modificationStopMillis;
	
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
}